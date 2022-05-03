<?php
/**
 * Add custom image size for app thaumbail image for produts etc
 */  
add_action( 'after_setup_theme', 'pgs_woo_api_app_custom_images' );
function pgs_woo_api_app_custom_images(){
    add_image_size( 'app_thumbnail', 260, 290, true );    
}

/**
 * Content Wrappers.
 *
 * @see pgs_woo_api_app_checkout_output_content_wrapper_start()
 * @see pgs_woo_api_app_checkout_output_content_wrapper_end() 
 */
add_action( 'pgs_woo_api_app_checkout_content_wrapper_start', 'pgs_woo_api_app_checkout_output_content_wrapper_start', 10 );
add_action( 'pgs_woo_api_app_checkout_content_wrapper_end', 'pgs_woo_api_app_checkout_output_content_wrapper_end', 10 );

function pgs_woo_api_app_checkout_output_content_wrapper_start(){
    $html  = '<div id="page" class="hfeed site">';
    $html .= '<div id="content" class="site-content">';
    $html .= '<div class="container">';
    echo $html; 
}

function pgs_woo_api_app_checkout_output_content_wrapper_end(){
    $html   = '</div>';
    $html  .= '</div>';
    $html  .= '</div><!-- .site -->';
    echo $html;
}

/**
* Create table for push_notification. 
*/
function pgs_woo_api_add_push_notification_tabel_schema(){
    global $wpdb;
    $push_table = $wpdb->prefix.'pgs_woo_api_notifications';
    if($wpdb->get_var("SHOW TABLES LIKE '$push_table'") != $push_table) {
         //table not in database. Create new table
        $charset_collate = $wpdb->get_charset_collate();
         $sql = "CREATE TABLE $push_table (
                id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                device_token text NOT NULL,
                device_type tinyint NOT NULL,
                status tinyint(1) NOT NULL DEFAULT '1'
            ) $charset_collate ENGINE = MYISAM";
         require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );
         dbDelta( $sql );
    }
    
    $push_meta_table = $wpdb->prefix.'pgs_woo_api_notifications_meta';
    if($wpdb->get_var("SHOW TABLES LIKE '$push_meta_table'") != $push_meta_table) {
         //table not in database. Create new table
        $charset_collate = $wpdb->get_charset_collate();
         $sql = "CREATE TABLE $push_meta_table (
                id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                msg text NOT NULL,
                custom_msg text NOT NULL,
                not_code tinyint NOT NULL,
                created datetime NULL
            ) $charset_collate ENGINE = InnoDB";
         require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );
         dbDelta( $sql );
    }
    
    $push_relationships = $wpdb->prefix.'pgs_woo_api_notifications_relationships';
    if($wpdb->get_var("SHOW TABLES LIKE '$push_relationships'") != $push_relationships) {
         //table not in database. Create new table
        $charset_collate = $wpdb->get_charset_collate();
        $push_relationships_ibfk_2 = $push_relationships.'_ibfk_2';
                
        $sql = "CREATE TABLE $push_relationships (
        id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
        not_id int(10) unsigned NOT NULL,
        user_id int(10) unsigned NOT NULL,
        push_meta_id bigint(20) NOT NULL,
        PRIMARY KEY (id),
        KEY push_meta_id (push_meta_id),
        CONSTRAINT $push_relationships_ibfk_2 FOREIGN KEY (push_meta_id) REFERENCES $push_meta_table (id) ON DELETE CASCADE
        ) $charset_collate ENGINE = InnoDB";
         require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );
         dbDelta( $sql );
    }
    
    $scratch_coupons = $wpdb->prefix.'pgs_woo_api_scratch_coupons';
    if($wpdb->get_var("SHOW TABLES LIKE '$scratch_coupons'") != $scratch_coupons) {
         //table not in database. Create new table
        $charset_collate = $wpdb->get_charset_collate();
        $sql = "CREATE TABLE $scratch_coupons (
        id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
        coupon_id int(10) unsigned NOT NULL,
        user_id int(10) unsigned NOT NULL,
        device_token text NOT NULL,
        is_coupon_scratched varchar(5) NOT NULL,
        PRIMARY KEY (id) )
        $charset_collate ENGINE = InnoDB";
        require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );
        dbDelta( $sql );
    }
    
    
    /**
    * Add custom table when plugin active for geofencing. 
    */    
    $table_name = $wpdb->prefix.'pgs_woo_api_geo_fencing';
    if($wpdb->get_var("SHOW TABLES LIKE '$table_name'") != $table_name) {
         //table not in database. Create new table
        $charset_collate = $wpdb->get_charset_collate();
         $sql = "CREATE TABLE $table_name (
                id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                post_id INT NOT NULL,
                radius DOUBLE NOT NULL,
                lat DOUBLE NOT NULL,
                lng DOUBLE NOT NULL,
                zoom int(4) NOT NULL
            ) $charset_collate ENGINE = MYISAM";
         require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );
         dbDelta( $sql );
    }
    
    
    $is_abandoned_cart = pgs_woo_api_is_abandoned_cart();
    if($is_abandoned_cart){
        /**
        * Add custom table when plugin active for geofencing. 
        */    
        $table_name = $wpdb->prefix.'pgs_woo_api_abandoned_cart_meta';
        if($wpdb->get_var("SHOW TABLES LIKE '$table_name'") != $table_name) {
             //table not in database. Create new table
            $charset_collate = $wpdb->get_charset_collate();
             $sql = "CREATE TABLE $table_name (
                    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                    abandoned_cart_id INT(11) NOT NULL,
                    cart_item_key text NOT NULL,
                    device_type varchar(15) NOT NULL,
                    device_token text NOT NULL
                ) $charset_collate ENGINE = MYISAM";
             require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );
             dbDelta( $sql );
        }
    }            
}

/**
* Add / Update data
*/
function pgs_woo_api_add_push_notification_data($device_token,$device_type,$user_id,$status=null){
    global $wpdb;    
    $table_name = $wpdb->prefix . "pgs_woo_api_notifications";            
    $qur = "SELECT * FROM $table_name WHERE device_token = '$device_token'";
    $results = $wpdb->get_results( $qur, OBJECT );
    
    if(isset($device_type) && $device_type == "android"){
        $device_type = 2;    
    }
    
    $data = array(
        'user_id' => $user_id,         		
        'device_token' => $device_token,
        'device_type' => $device_type,             
    );     
    $formate = array(            		
            '%d',
            '%s',
            '%d'
    );
    if($status != null){
        $data['status'] = $status;
        $formate[] = '%d';
    }
        
    if(!empty($results)){           
        $wpdb->update($table_name, $data, array('device_token' => $device_token),$formate,array('%s'));
    } else {        
        $wpdb->insert( $table_name,$data,$formate );
    }
}

function pgs_woo_api_push_status(){
    $pushstatus = get_option('pgs_push_status');
    $push_status = (isset($pushstatus) && !empty($pushstatus))?$pushstatus:'enable';
    if($push_status == 'enable'){
        return true;    
    } else {
        return false;
    }
}

/**
* Get All data
*/
function pgs_woo_api_get_push_notification_data($user_id){
    global $wpdb;    
        
    $table_name = $wpdb->prefix . "pgs_woo_api_notifications";            
    $where = '';
    if($user_id > 0){
        $where = ' WHERE user_id = '.$user_id;    
    }    
    $qur = "SELECT * FROM $table_name".$where;
    $results = $wpdb->get_results( $qur, OBJECT );        
    return $results;
}

/**
* Get notification data
*/
function pgs_woo_api_get_notificationi_data($not_key){
    
    $key = (string)$not_key; 
    $pgs_not_code = get_option('pgs_not_code');
    $title = '';$message='';
    if(isset($pgs_not_code) && !empty($pgs_not_code)){
        $title = (isset($pgs_not_code[$key]['title']))?$pgs_not_code[$key]['title']:'';
        $message = (isset($pgs_not_code[$key]['message']))?$pgs_not_code[$key]['message']:'';    
    }
    return array(
        'title' => $title,
        'message' => $message 
    );
}

/**
 * Check Whishlist plugin is activated
 */ 
function pgs_woo_api_is_wishlist_active(){     
    $plugin = 'yith-woocommerce-wishlist/init.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

/**
 * Check Currency Switcher plugin is activated
 */ 
function pgs_woo_api_is_currency_switcher_active(){     
    $plugin = 'woocommerce-currency-switcher/index.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

/**
 * Check Order Tracking plugin is activated
 */ 
function pgs_woo_api_is_order_tracking_active(){     
    $plugin = 'aftership-woocommerce-tracking/aftership.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

/**
 * Check woocommerce-abandoned-cart plugin is activated
 */ 
function pgs_woo_api_is_abandoned_cart(){     
    $plugin = 'woocommerce-abandoned-cart/woocommerce-ac.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}


/**
 * Check Order Tracking plugin is activated
 */ 
function pgs_woo_api_is_reward_points_active(){     
    $plugin = 'woocommerce-points-and-rewards/woocommerce-points-and-rewards.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

/**
 * Check dokan plugin is activated
 */ 
function pgs_woo_api_is_dokan_active(){     
    $plugin = 'dokan-lite/dokan.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

/**
 * Check dokan pro plugin is activated
 */ 
function pgs_woo_api_is_dokan_pro_active(){     
    $plugin = 'dokan-pro/dokan-pro.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

/**
 * Check WC Marketplace plugin is activated
 */ 
function pgs_woo_api_is_wc_marketplace_active(){     
    $plugin = 'dc-woocommerce-multi-vendor/dc_product_vendor.php';    
    if (is_plugin_active($plugin)) {        
        return true;
    } else {        
        return false;
    }   
}

function pgs_woo_api_is_vendor_plugin_active(){
    
    $is_vendor = pgs_woo_api_is_dokan_active(); 
    $is_vendor_2 = pgs_woo_api_is_wc_marketplace_active();
    if(!$is_vendor && !$is_vendor_2) {
        $data = array(
            'vendor_active' => false,
            'vendor_count' => 0,            
        );
        return $data; 
    }
    
    $cnt = 0;
    if($is_vendor) {
        $cnt += 1;
        $data = array(
            'vendor_active' => true,
            'vendor_count' => 1,
            'vendor_for' => 'dokan',
        );
    } 
    if ($is_vendor_2) {        
        $cnt += 1;
        $data = array(
            'vendor_active' => true,
            'vendor_count' => 1,
            'vendor_for' => 'wc_marketplace',
        );         
    }
    if($cnt == 2){
        $activevendor = get_option('pgs_active_vendor');
        $active_vendor = (isset($activevendor) && !empty($activevendor))?$activevendor:'dokan';
        $data = array(
            'vendor_active' => true,
            'vendor_count' => 2,
            'vendor_for' => $active_vendor,
        );        
    }
    return $data;     
}
 

/**
 * Remove include tax html in price html
 */ 
function pgs_woo_api_hook_remove_tax_in_price_html(){
    add_filter( 'woocommerce_get_price_html', 'pgs_remove_tax_in_price_html', 100, 2 );    
}
function pgs_remove_tax_in_price_html( $price, $product ){    
    $html = preg_replace('# <small class="woocommerce-price-suffix"(.*?)</small>#', '', $price);
    return $html;
}

/**
* Update currency rale if currency switcher plugin is active
* Call this filter for default woocommerce price on variations product. 
*/
add_filter( "woocommerce_rest_prepare_product_variation_object", 'pgs_woo_api_woocommerce_rest_prepare_product_object',10,3 );
function pgs_woo_api_woocommerce_rest_prepare_product_object( $response, $object, $request ){    
    $is_currency_switcher_active = pgs_woo_api_is_currency_switcher_active();
    if($is_currency_switcher_active){
        $get_price = pgs_woo_api_update_currency_rate_for_default_api($response->data['price']);
        $regular_price = pgs_woo_api_update_currency_rate_for_default_api($response->data['regular_price']);
        $sale_price = pgs_woo_api_update_currency_rate_for_default_api($response->data['sale_price']);
        
        $response->data['price'] = $get_price;  
        $response->data['regular_price'] = $regular_price;
        $response->data['sale_price'] = $sale_price;
    }
    return apply_filters( "pgs_woo_api_woocommerce_rest_prepare_product_object", $response, $object, $request );
}


function pgs_woo_api_update_currency_rate_for_default_api($price){
        
    global $WOOCS;
    $currencies = $WOOCS->get_currencies();
    if(!empty($price)){
        $price = $price * $currencies[$WOOCS->current_currency]['rate'];
        $price = number_format($price, 2, $WOOCS->decimal_sep, '');    
    }
    return $price;
}


/**
 * Get Woocommerce price option data for price formating
 */
function get_woo_price_formate_option_array(){
    $currency_pos = get_option( 'woocommerce_currency_pos' );
    $arr = array(
        'decimal_separator'  => wc_get_price_decimal_separator(),
        'thousand_separator' => wc_get_price_thousand_separator(),
        'decimals'           => wc_get_price_decimals(),
        'currency_pos'       => $currency_pos,
        'currency_symbol'    => get_woocommerce_currency_symbol(),
        'currency_code' => get_woocommerce_currency()
    );
    return $arr;
}

// WooCoommece Rest API for set price html in
add_filter('woocommerce_rest_prepare_product_variation_object','pgs_api_woocommerce_rest_prepare_product_variation_object',10,3);
function pgs_api_woocommerce_rest_prepare_product_variation_object($response, $object, $request){ 
    $html = $object->get_price_html();    
    $price_html = preg_replace('# <small class="woocommerce-price-suffix"(.*?)</small>#', '', $html);
    $response->data['price_html'] = $price_html;    
    return $response;
}

function pgs_woo_script_style_admin() {
    
    wp_register_style( 'pgs-woo-api-css', PGS_API_URL.'css/pgs-woo-api.css' );
    wp_register_style( 'pgs-woo-api-geofance-css', PGS_API_URL.'css/geofance.css' );    
    wp_register_style( 'jquery-ui', PGS_API_URL.'css/jquery-ui.min.css' );
    
    wp_register_script('jquery-repeater-min', PGS_API_URL.'js/jquery.repeater.min.js', array(), false, false );
    wp_register_script('pgs-woo-api-js', PGS_API_URL.'js/pgs-woo-api.js', array('jquery-ui-core','jquery-ui-tabs','jquery-ui-datepicker','media-upload','thickbox','wp-color-picker','jquery-repeater-min'), false, false );
    wp_localize_script( 'pgs-woo-api-js', 'pgs_woo_api', array(
    	'plugin_url' => plugins_url(),
    	'pgs_api_url' => PGS_API_URL,
        'delete_msg' => esc_html__("Are you sure you want to delete this element?",'pgs-woo-api'),
        'choose_image' => esc_html__("Choose Image",'pgs-woo-api'),
        'add_image' => esc_html__( 'Add Image','pgs-woo-api')
    ) );
    $google_key = pgs_woo_api_get_google_map_api_key();
    wp_register_script( 'pgs-woo-api-google-maps-apis' , 'https://maps.googleapis.com/maps/api/js?key='.$google_key.'&libraries=drawing,places&callback=geoFenc', array(), false, true );
    wp_register_script( 'pgs-woo-api-geofance' , PGS_API_URL.'js/geofance.js', array(),false,true );
    
    if( ( isset( $_GET['page'] ) && $_GET['page'] == 'pgs-woo-api-settings' ) || 
        ( isset( $_GET['page'] ) && $_GET['page'] == 'pgs-woo-api-token-settings' ) ||        
        ( isset( $_GET['page'] ) && $_GET['page'] == 'pgs-woo-api-geo-fencing-settings' )) {        
        wp_enqueue_style( 'pgs-woo-api-css' );
    }
    
    if( ( isset( $_GET['page'] ) && $_GET['page'] == 'pgs-woo-api-settings' ) || 
        ( isset( $_GET['page'] ) && $_GET['page'] == 'pgs-woo-api-token-settings' ) || 
        ( isset($_GET['taxonomy']) && $_GET['taxonomy'] == 'product_cat') ||
        ( isset($_GET['taxonomy']) && $_GET['taxonomy'] == 'pa_color') ||
        ( isset( $_GET['page'] ) && $_GET['page'] == 'pgs-woo-api-geo-fencing-settings' )) {        
        wp_enqueue_media();        
        wp_enqueue_style( 'thickbox' );
        wp_enqueue_style( 'jquery-ui' );        
        wp_enqueue_style( 'wp-color-picker' ); 
        wp_enqueue_script( 'jquery-repeater-min' );        
        //wp_localize_script( 'pgs-woo-api-js', 'pgs_app_plugin_url', PGS_API_URL );        
        wp_enqueue_script( 'pgs-woo-api-js' );
    }
}
add_action( 'admin_enqueue_scripts', 'pgs_woo_script_style_admin' );


function pgs_woo_api_get_google_map_api_key(){
    $google_map_api_key = '';
    $pgs_google_keys = get_option('pgs_google_keys');    
    if(isset($pgs_google_keys['google_keys']['google_map_api_key']) && !empty($pgs_google_keys['google_keys']['google_map_api_key'])){        
        $google_map_api_key = $pgs_google_keys['google_keys']['google_map_api_key'];        
    }
    return $google_map_api_key;
}

/**
 * Get app color for app checkput page.
 */ 
function pgs_woo_api_get_app_color(){
    $app_color = array(
		'primary_color' => '#60A727',
		'secondary_color' => ''
	);
    $app_assets = get_option('pgs_woo_api_app_assets_options');
    if(isset($app_assets) && !empty($app_assets)){
        if(isset($app_assets['app_assets']['app_color']) && !empty($app_assets['app_assets']['app_color'])){                
            $app_color = $app_assets['app_assets']['app_color'];
        }
    }
    return $app_color;    
}
/**
 * Filter for call custom checkout page 
 */
add_filter( 'page_template', 'pgs_woo_api_app_checkout_page' );
function pgs_woo_api_app_checkout_page( $page_template ){
    
    $checkout_page = get_option('pgs_checkout_page');
    if(isset($checkout_page) && !empty($checkout_page)){
        $checkoutpage = get_post($checkout_page);        
        if(isset($checkoutpage))
        $appcheckout = $checkoutpage->post_name;    
    }
     
    if(isset($appcheckout) && !empty($appcheckout)){        
        if ( is_page( $appcheckout ) ) {
            add_filter('woocommerce_is_checkout',function (){ return true; });            
            $page_template = PGS_API_PATH . 'template/app_checkout.php';
        }
    }
    return $page_template;
}

function pgs_woo_api_get_app_checkout_page(){
    global $woocommerce;
    $checkout_url = '';
    $checkout_url = $woocommerce->cart->get_checkout_url();        
    $checkout_page = get_option('pgs_checkout_page');
    if(isset($checkout_page) && !empty($checkout_page)){        
        $checkouturl = get_permalink($checkout_page);
        if(isset($checkouturl) && !empty($checkouturl)){
            $checkout_url = $checkouturl;    
        }                        
    }
    return $checkout_url;
}

function pgs_woo_api_get_page_title_for_slug($page_slug) {

     $page = get_page_by_path( $page_slug , OBJECT );

     if ( isset($page) )
        return $page;
     else
        return false;
}


function pgs_woo_api_get_all_woo_cat(){
    $taxonomy     = 'product_cat';
    $orderby      = 'name';  
    $show_count   = 0;      // 1 for yes, 0 for no
    $pad_counts   = 0;      // 1 for yes, 0 for no
    $hierarchical = 1;      // 1 for yes, 0 for no  
    $title        = '';  
    $empty        = 0;
    
    $args = array(
         'taxonomy'     => $taxonomy,
         'orderby'      => $orderby,
         'show_count'   => $show_count,
         'pad_counts'   => $pad_counts,
         'hierarchical' => $hierarchical,
         'title_li'     => $title,
         'hide_empty'   => $empty
    );
    $all_categories = get_categories( $args );
    $opt = array();
    foreach ($all_categories as $cat) {
        if($cat->category_parent == 0) {
            $category_id = $cat->term_id;            
            $opt[$category_id] = $cat->name;
        
            $args2 = array(
                    'taxonomy'     => $taxonomy,
                    'child_of'     => 0,
                    'parent'       => $category_id,
                    'orderby'      => $orderby,
                    'show_count'   => $show_count,
                    'pad_counts'   => $pad_counts,
                    'hierarchical' => $hierarchical,
                    'title_li'     => $title,
                    'hide_empty'   => $empty
            );
            $sub_cats = get_categories( $args2 );
            if($sub_cats) {
                foreach($sub_cats as $sub_category) {                    
                    $opt[$sub_category->term_id] = "- ".$sub_category->name;
                }   
            }
        }       
    }
    return $opt;
}

/**
 * Notification message 
 */
function pgs_woo_api_admin_notice_render($message,$status) {           
    $html = '<div class="notice notice-'.$status.' is-dismissible">';
        $html .= '<p>'.$message.'</p>';
    $html .= '</div>';
    return $html;
}

/**
 * Get feature box option status  
 */
function pgs_woo_api_feature_box_status(){
    $pgs_woo_api_home_option = get_option('pgs_woo_api_home_option');
    $feature_box_status = (isset($pgs_woo_api_home_option['feature_box_status']) && !empty($pgs_woo_api_home_option['feature_box_status']))?$pgs_woo_api_home_option['feature_box_status']:'enable';       
    if($feature_box_status == 'enable'){
        $style = 'style="display: block;"';
    } else {
        $style = 'style="display: none;"';
    }
    echo $style;    
}

/**
 * Send checkout page url for android
 * */
add_action( 'wp_footer' , 'pgs_woo_api_add_to_cart_android' );
function pgs_woo_api_add_to_cart_android(){    
    
    $input = file_get_contents("php://input");
    $request = json_decode($input,true);
    
    $cart_items = $request['cart_items'];        
    if(isset($request['os']) && $request['os'] == 'android'){
        if(isset($cart_items)){                            
            $url = pgs_woo_api_get_app_checkout_page();
            ?>
            <script type="text/javascript">
                showAndroidToast( '<?php echo esc_url($url)?>');       
                function showAndroidToast(toast) {            
                    Android.showToast(toast);
                }
            </script><?php
        }
    }      
}

function token_generations_pro(){
    $url = home_url('wp-json');    
    $method = 'GET';
    $pgs_woo_api = get_option('app_auth');    
    $client_key='';$client_secret='';$token='';$token_secret='';
    if(isset($pgs_woo_api['pgs_auth']) && !empty($pgs_woo_api['pgs_auth'])){
        $pgs_auth = $pgs_woo_api;
        $client_key = (isset($pgs_auth['pgs_auth']['client_key']))?$pgs_auth['pgs_auth']['client_key']:'';
        $client_secret = (isset($pgs_auth['pgs_auth']['client_secret']))?$pgs_auth['pgs_auth']['client_secret']:'';          
    }
    if( $client_key != '' && $client_secret != '' ){
        $auth_data = array(
    		'oauth_consumer_key'    => $client_key,
    		'oauth_consumer_secret' => $client_secret,    		
    	);        
    	$step = 0;
        $oauth_verifier = '';
        if(isset($_POST['step']) && $_POST['step'] == 1){
            $step = $_POST['step'];
            $url = home_url('wp-json');            
        } elseif(isset($_POST['step']) && $_POST['step'] == 2){
            $step = $_POST['step'];
            $oauth_verifier = trim($_POST['oauth_verifier']);
            $oauth_consumer_key = $client_key;    
    		$oauth_consumer_secret = $client_secret; 
            $oauth_token = $_POST['oauth_token']; 
            $oauth_token_secret = $_POST['oauth_token_secret'];
            $auth_data = array(
        		'oauth_consumer_key'    => $oauth_consumer_key,
        		'oauth_consumer_secret' => $oauth_consumer_secret,
                'oauth_token' => $oauth_token,
                'oauth_token_secret' => $oauth_token_secret                                 
        	);            
            $url = home_url('oauth1/access');                    
        }        
        if(isset($_POST)){        
            $tokenGenerations = new PGS_WOO_API_TokenGenerationsController( $auth_data, $url, $method, $step, $oauth_verifier  );
        }
    } else {
        esc_html_e('Please enter client key, client secret from Users -> Applications','pgs-woo-api');        
    }
}

/**
 * Call test api
 */ 
add_action( 'wp_ajax_pgs_woo_api_test_api_ajax_call', 'pgs_woo_api_test_api_ajax_call' );
function pgs_woo_api_test_api_ajax_call(){        
    echo do_shortcode( '[pgs_woo_api_check_oauth_connection]' );
    exit();
}

/**
 * Add abadonded cart meta for send push notification 
 */ 
function pgs_woo_api_add_abadonded_cart_meta($cart_item_key,$device_type,$device_token){        
    global $wpdb;
    $abandoned_cart_meta = $wpdb->prefix."pgs_woo_api_abandoned_cart_meta";
    if(isset($_SESSION['abandoned_cart_id_lite']) && !empty($_SESSION['abandoned_cart_id_lite'])){            
        $ddd = $wpdb->insert( 
        	$abandoned_cart_meta, 
        	array( 
        		'abandoned_cart_id' => $_SESSION['abandoned_cart_id_lite'], 
        		'cart_item_key' => $cart_item_key,
                'device_type' => $device_type,
                'device_token' => $device_token
        	), 
        	array('%d','%s','%s','%s') 
        );
    }
}