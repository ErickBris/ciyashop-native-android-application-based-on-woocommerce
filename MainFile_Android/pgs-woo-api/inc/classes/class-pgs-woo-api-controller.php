<?php
if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

class PGS_WOO_API_Controller {
    
    protected $push_table = "pgs_woo_api_notifications";            
    protected $push_meta_table = "pgs_woo_api_notifications_meta";
    protected $push_relation_table = "pgs_woo_api_notifications_relationships";
    
    
    public function pgs_woo_api_permission_callback() {        
        return current_user_can( 'update_core' );   
    }    
    
    /**
    * Validate required fields
    */    
    protected function pgs_woo_api_param_validation($paramarray,$data) {
    	$NovalueParam = '';
        
        if(isset($data) && !empty($data)){
            $data = $data;    
        } else {
            $data = array();
        }
        
        foreach($paramarray as $val) {
    		if(!array_key_exists($val,$data)) {				
    			$NovalueParam[] = $val; 
    		}
    	}
    	if(is_array($NovalueParam) && count($NovalueParam)>0) {
    		$returnArr['error'] = "error";
    		$returnArr['message'] = esc_html__('Sorry, that is not valid input. You missed '.implode(',',$NovalueParam).' parameters','pgs-woo-api');
    		return $returnArr;
    	} else { 
    		return false;
    	}
    }
    
    
    /**
     * Get All Popular Products     
     */ 
    public function pgs_woo_api_get_popular_products(){        
    
    	global $wpdb;
		
		$querystr = "SELECT $wpdb->posts.ID
		FROM $wpdb->posts
		INNER JOIN $wpdb->postmeta ON ( $wpdb->posts.ID = $wpdb->postmeta.post_id )
		WHERE 1=1
		AND ( $wpdb->postmeta.meta_key = 'total_sales' )
		AND $wpdb->posts.post_type = 'product'
		AND (($wpdb->posts.post_status = 'publish'))
		GROUP BY $wpdb->posts.ID ORDER BY $wpdb->postmeta.meta_value+0 DESC
		LIMIT 0, 10";
		$popular_products = $wpdb->get_results($querystr, ARRAY_A);
		
        $productdata = array();
        
        if(!empty($popular_products)):
            foreach($popular_products as $product){   
			
                
                $product_id = $product['ID'];
                if (has_post_thumbnail( $product_id )){
                    $image = '';
                    $image = get_the_post_thumbnail_url($product_id, apply_filters( 'pgs_woo_api_app_thumbnail_image', 'app_thumbnail' ));
                    if(empty($image)){
                        $image = woocommerce_placeholder_img_src();    
                    } 
                } else {
                  $image = woocommerce_placeholder_img_src();  
                }
                
                pgs_woo_api_hook_remove_tax_in_price_html();//Remove include tax in price html
                $product = wc_get_product( $product_id );
                $price_html = $product->get_price_html();
                $regular_price = $product->get_regular_price();
                $sale_price = $product->get_sale_price();
                $get_price = $product->get_price();
                
                $is_currency_switcher_active = pgs_woo_api_is_currency_switcher_active();
                if($is_currency_switcher_active){
                    $regular_price = $this->pgs_woo_api_update_currency_rate($regular_price);
                    $sale_price = $this->pgs_woo_api_update_currency_rate($sale_price);
                    $get_price = $this->pgs_woo_api_update_currency_rate($get_price);
                }
                
                $price = array(
                    'regular_price' => $regular_price,
                    'sale_price' => $sale_price,
                    'price' => $get_price
                );
                $average = $product->get_average_rating();
                
                $productdata[] = array(
                    'id' => $product_id,
                    'title' => html_entity_decode(get_the_title($product_id)),
                    'image' => $image,                
                    'price_html' => $price_html,
                    'price' => $price,                    
                    'rating' => $average,                      
                );            
            }
            
        else :        
            $error['message'] = esc_html__("No product found","pgs-woo-api");
            return $error;    
        endif;    
        return $productdata;
    }
    
    /**
     * Update currency rale if currency switcher plugin is active 
     */
    public function pgs_woo_api_update_currency_rate($price){
        
        global $WOOCS;
        $currencies = $WOOCS->get_currencies();
        if(!empty($price)){
            $price = $price * $currencies[$WOOCS->current_currency]['rate'];
            $price = number_format($price, 2, $WOOCS->decimal_sep, '');    
        }
        return $price;
    }
    
    /**
     * Gey All Scheduled Sale Products     
     */ 
    public function pgs_woo_api_scheduled_sale_products(){
        
        global $wpdb,$woocommerce;
        $error = array( "status" => "error" );
        $productdata = array();     
        $qur = "SELECT posts.ID, posts.post_parent
        FROM $wpdb->posts posts
        INNER JOIN $wpdb->postmeta ON (posts.ID = $wpdb->postmeta.post_id)
        INNER JOIN $wpdb->postmeta AS mt1 ON (posts.ID = mt1.post_id)
        WHERE
            posts.post_status = 'publish'
            AND  (mt1.meta_key = '_sale_price_dates_to' AND mt1.meta_value >= ".time().")
            
            GROUP BY posts.ID 
            ORDER BY posts.post_title ASC";
        $product_ids_raw = $wpdb->get_results( $qur );
        
        $product_ids_on_sale = array();
        $image = '';
        foreach ( $product_ids_raw as $product_raw ) 
        {
            if(!empty($product_raw->post_parent))
            {
                $product_ids_on_sale[] = $product_raw->post_parent;
            }
            else
            {
                $product_ids_on_sale[] = $product_raw->ID;              
            }
        }
        $product_ids_on_sale = array_unique($product_ids_on_sale);
            
        if ( !empty( $product_ids_on_sale ) ) {        
            
                foreach($product_ids_on_sale as $val){
                         
                    $product_id = $val;
                
                    if (has_post_thumbnail( $product_id )){
                        $image = get_the_post_thumbnail_url($product_id);    
                    } else {
                        $image = woocommerce_placeholder_img_src();               
                    }
                    $from = get_post_meta($product_id,'_sale_price_dates_from',true);                    
                        
                    $now = new DateTime();
                    $future_date = new DateTime(date('Y-m-d').' 24:00:00');
                    
                    $product = wc_get_product( $product_id );
                    $price_html = $product->get_price_html();
                    $regular_price = $product->get_regular_price();
                    $sale_price = $product->get_sale_price();
                    $get_price = $product->get_price();
                    
                    /**
                     * Update currency rale if currency switcher plugin is active 
                     */ 
                    $is_currency_switcher_active = pgs_woo_api_is_currency_switcher_active();
                    if($is_currency_switcher_active){
                        global $WOOCS;
                        $currencies = $WOOCS->get_currencies();
                        if(!empty($regular_price)){
                            $regular_price = $regular_price * $currencies[$WOOCS->current_currency]['rate'];
                            $regular_price = number_format($regular_price, 2, $WOOCS->decimal_sep, '');    
                        }
                        
                        if(!empty($regular_price)){
                            $sale_price = $sale_price * $currencies[$WOOCS->current_currency]['rate'];
                            $sale_price = number_format($sale_price, 2, $WOOCS->decimal_sep, '');     
                        }
                        
                        if(!empty($get_price)){
                            $get_price = $get_price * $currencies[$WOOCS->current_currency]['rate'];
                            $get_price = number_format($get_price, 2, $WOOCS->decimal_sep, '');     
                        }   
                    }
                    
                    
                    $price = array(
                        'regular_price' => $regular_price,
                        'sale_price' => $sale_price,
                        'price' => $get_price,
                    );
                    $per = 0;
                    if( $product->is_type( 'simple' ) ){
                        if($regular_price > 0 && $sale_price > 0){
                            $per = round((($regular_price - $sale_price) / ($regular_price)) * 100);
                        }
                    } elseif( $product->is_type( 'variable' ) ){

            			$available_variations = $product->get_available_variations();
            
            			if($available_variations){
            
            				$percents = array();
            				foreach($available_variations as $variations){
            
            					$regular_price = $variations['display_regular_price'];
            					$sale_price = $variations['display_price'];
            
            					if ($regular_price){
            						$percentage = round( ( ( $regular_price - $sale_price ) / $regular_price ) * 100 );
            						$percents[] =  $percentage;
            					}
            				}
            
            				$max_discount = min($percents);
            				$per = $max_discount;
            			}
            		}
                    
                    $interval = $future_date->diff($now);                            
                    $deal_life = array(                        
                        'hours' => $interval->format('%h'),
                        'minutes' => $interval->format('%i'),
                        'seconds' => $interval->format('%s')
                    );  
                    
                    
                    $average = $product->get_average_rating();
                              
                    if($from <= time()){
                        $productdata[] = array(
                            'id' => $product_id,
                            'title' => html_entity_decode(get_the_title($product_id)),
                            'image' => $image,
                            'deal_life' => $deal_life,
                            'price_html' => $price_html,
                            'price' => $price,
                            'percentage' => $per,                     
                            'rating' => $average,                      
                        );
                    }                
                }
            
            return array( 
                "status" => "success",
                "products" => $productdata                     
            );    
        } else {
            
            $error['message'] = esc_html__("No product found","pgs-woo-api");
            return $error;                
        }         
    }
    
    /**
    * Send Pushnotification 
    */
    public function send_push($msg, $badge, $custom_msg,$not_code,$device_data) {

        $pushstatus = get_option('pgs_push_status');
        $push_status = (isset($pushstatus) && !empty($pushstatus))?$pushstatus:'enable';        
        if($push_status != 'enable'){
            return;
        }
            
        $pushmode = get_option('pgs_push_mode');
        $push_mode = (isset($pushmode) && !empty($pushmode))?$pushmode:'live';        
        $filter_array_ios = array();
        $filter_array_ios = array_filter($device_data, array($this,'filter_array_ios_arr'));
        $filter_array_android = array_filter($device_data, array($this,'filter_array_android'));         
                        
        if( !empty($filter_array_ios )){            
            $this->ios( $filter_array_ios,$msg, $badge, $custom_msg,$not_code,$push_mode );        			
        } 
        
        if( !empty($filter_array_android )){
            $this->android( $filter_array_android, $msg, $badge, $custom_msg, $not_code );
        }                            
	}
    
    /**
    * Send Pushnotification for IOS 
    */
    public function ios($devicetokens,$msg, $badge, $custom_msg,$not_code,$push_mode){
        $pem_file = '';$pem_file_pass = '';
        if($push_mode == 'sandbox'){    			
            $gateway = 'ssl://gateway.sandbox.push.apple.com:2195';
            $pem_file_dev = get_option('pem_file_dev');
            $pem_file =  (isset($pem_file_dev))?$pem_file_dev:'';
            $pemfiledevpass = get_option('pem_file_dev_pass');
            $pem_file_pass = (isset($pemfiledevpass) && !empty($pemfiledevpass))?$pemfiledevpass:'';

        } else {
			$gateway = 'ssl://gateway.push.apple.com:2195';
            $pem_file_pro = get_option('pem_file_pro');
            $pem_file = (isset($pem_file_pro))?$pem_file_pro:'';            
            $pemfilepropass = get_option('pem_file_pro_pass');
            $pem_file_pass = (isset($pemfilepropass) && !empty($pemfilepropass))?$pemfilepropass:'';
		}
        if($pem_file != ''){
            $pem = PGS_API_PATH.'inc/options-pages/pem/'.$pem_file;
            //$aumsg = 'Connected to APNS' . PHP_EOL . '<hr>';
    
    		// Create the payload body
    		$body['aps'] = array(
    			'alert' => array(
    			    'title'    => $msg,
                    'body'     => $custom_msg,
                    'badge'    => $badge,
                    'not_code' => $not_code
    			 ),
    			'sound' => 'default'
    		);
    
    		// Encode the payload as JSON
    		$payload = json_encode($body);
            
            $lastid = 0;
    		if($not_code != 0){ // check for test notification zero for test notification
                $lastid = $this->add_notification_meta($msg,$custom_msg,$not_code);  
    		}                
            foreach($devicetokens as $devicetoken){
                $token = $devicetoken['token'];
                $token_ststus = $this->is_notifiction_on_to_token($token);
                if(isset($token_ststus) && $token_ststus == 1){                                            
                    $ctx = stream_context_create();                    
            		stream_context_set_option($ctx, 'ssl', 'local_cert', $pem );
            		stream_context_set_option($ctx, 'ssl', 'passphrase', $pem_file_pass);    
            		$fp = stream_socket_client( $gateway, $err,$errstr, 60, STREAM_CLIENT_CONNECT|STREAM_CLIENT_PERSISTENT, $ctx);            
            		
                    if (!$fp){
                        //exit("Failed to connect: $err $errstr" . PHP_EOL);
                    }                   
                    @$msgft = chr(0) . pack('n', 32) . pack('H*', $token) . pack('n', strlen($payload)) . $payload;        
                    $write_result = fwrite($fp, $msgft, strlen($msgft));        
                    if (!$write_result){
                        //$aumsg.='Message to not delivered' . PHP_EOL . '<hr>';
                    } else {
                        //$aumsg.='Message to successfully delivered' . PHP_EOL . '<hr>';
                    }                    
                    fclose($fp);
                }
                if($not_code != 0){
                    $this->add_push_relation($token,$lastid);
                }
    		}
    		//$aumsg.='close connection';        
            // Close the connection to the server    		
        }		
    }
    
    
    /**
    * Sends Push notification for Android users 
    */
    public function android( $devicetokens,$msg, $badge, $custom_msg,$not_code ) {
        
        $android_l_s_key = get_option('android_l_s_key');
        $android_key = (isset($android_l_s_key))?$android_l_s_key:'';        
        if($android_key != ''){
            $url = 'https://fcm.googleapis.com/fcm/send';
            $message = array(
                'title' => $msg,
                'body' => $custom_msg                           
            );
            
            $data = array(        					
				'title' => $msg,
                'message' => $custom_msg,
                'not_code' => $not_code	
			);
            
            $headers = array(
            	'Authorization: key=' .$android_key,
            	'Content-Type: application/json'
            );
        
            
            
            $lastid = 0;
    		if($not_code != 0){ // check for test notification zero for test notification
                $lastid = $this->add_notification_meta($msg,$custom_msg,$not_code);  
    		} 
            foreach($devicetokens as $devicetoken){
                $token = $devicetoken['token']; 
                $token_ststus = $this->is_notifiction_on_to_token($token);
                if(isset($token_ststus) && $token_ststus == 1){
                    $fields = array(
                        'registration_ids' => array($token),                        
                        'notification'     => $message,
        				'data'             => $data,
                    );
                    $this->useCurl($url, $headers, json_encode($fields));
                }
                if($not_code != 0){
                    $this->add_push_relation($token,$lastid);
                }
            }
            
            /**
             * Start : Send-Notification for test notification Api
             */
            if($not_code == 0){
                $fields = array(
                    'registration_ids' => array($token),
                    'notification'     => $message,
    				'data'             => $data,
                );
                $this->useCurl($url, $headers, json_encode($fields));
            }
            /** End */        	    
        }
        return true;
    }
    
    // Curl 
	private function useCurl( $url, $headers, $fields = null) {
	         
            
            // Open connection
	        $ch = curl_init();
	        if ($url) {
	            // Set the url, number of POST vars, POST data
	            curl_setopt($ch, CURLOPT_URL, $url);
	            curl_setopt($ch, CURLOPT_POST, true);
	            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	     
	            // Disabling SSL Certificate support temporarly
	            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	            if ($fields) {
	                curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
	            }
	     
	            // Execute post
	            $result = curl_exec($ch);
	            if ($result === FALSE) {
	                //die('Curl failed: ' . curl_error($ch));
	            }
                
                // Close connection
	            curl_close($ch);
	
	            return $result;
        }
    }
    
    public function is_notifiction_on_to_token($token){
        global $wpdb;    
        $push_table = $wpdb->prefix . $this->push_table;        
        $status = 0;
        $qur = "SELECT status FROM $push_table WHERE device_token = '$token'";
        $results = $wpdb->get_row( $qur, OBJECT );        
        if(isset($results->status)){
            $status = $results->status;
        }
        return $status;
    }
    
    //Add notification meta for notification log
    public function add_notification_meta($msg,$custom_msg,$not_code){
            global $wpdb; 
            $push_meta_table = $wpdb->prefix . $this->push_meta_table;
            $metadata = array(                
                'msg' => sanitize_text_field($msg),
                'custom_msg' => sanitize_text_field($custom_msg),
                'not_code' => sanitize_text_field($not_code),
                'created' => date("Y-m-d H:i:s") 
            );                 
            $metaformate = array('%s','%s','%d','%s'); 
            $wpdb->insert( $push_meta_table,$metadata,$metaformate );
            $lastid = $wpdb->insert_id;
            return $lastid;            
    } 
    
    //Add push_relation for multiple divice token
    public function add_push_relation($token,$lastid){
        global $wpdb;    
        $push_table = $wpdb->prefix . $this->push_table;        
        $push_relation_table = $wpdb->prefix . $this->push_relation_table;
        
        $qur = "SELECT * FROM $push_table WHERE device_token = '$token'";
        $results = $wpdb->get_results( $qur, OBJECT );        
                   
        if(!empty($results)){           
                       
            foreach($results as $result){
                $data = array(
                    'not_id' => $result->id,         		
                    'user_id' => $result->user_id,
                    'push_meta_id' => $lastid,
                    
                );                 
                $formate = array('%d','%d','%d'); 
                $wpdb->insert( $push_relation_table,$data,$formate );                
            }            
        }
    }
    
    public function filter_array_ios_arr($value){
        return ($value['type'] == 1);
    }
    
    private function filter_array_android($value){
        return ($value['type'] == 2);
    }
    
    
    /**
    * Vendor plugin Short info for product listing page
    */
    public function pgs_woo_api_get_seller_short_details($product_id){
        
        $data = array(
            'is_seller' => false                
        );
        $author_id  = get_post_field( 'post_author', $product_id );
        
        $is_vendeo = pgs_woo_api_is_vendor_plugin_active();
        //echo '==>'.$is_vendeo['vendor_for'];
        if($is_vendeo['vendor_count'] > 0){
            if($is_vendeo['vendor_for'] == 'dokan'){
                $data = $this->get_dokan_vender_short_info($author_id);
            } else {
                $data = $this->get_wc_marketplace_vender_short_info($author_id,$product_id);    
            }            
        }
        return $data;
    }
    
    /**
    * Dokan plugin vendor Short info for product listing page
    */    
    public function get_dokan_vender_short_info($author_id){
        $info       = get_user_meta( $author_id, 'dokan_profile_settings', true );
        $data = array();
        if(isset($info) && !empty($info)){
            $author     = get_user_by( 'id', $author_id );
            $store_info = dokan_get_store_info( $author->ID );
            $seller_address = dokan_get_seller_address( $author->ID );
            $seller_rating = dokan_get_seller_rating( $author->ID );
            $store_tnc = (isset($store_info['store_tnc']))?nl2br($store_info['store_tnc']):'';
                        
            //check support status on off
            $contact_seller = $this->pgs_woo_api_contact_seller_status('dokan');            
            
            
            $data = array(
                'is_seller' => true,
                'seller_id' => $author_id,
                'store_name' => $store_info['store_name'],
                'address' => $store_info['address'],
                'seller_address' => $seller_address,
                'seller_rating' => $seller_rating,
                'store_tnc' => $store_tnc,
                'contact_seller' => $contact_seller 
            );
        } else {
            $data = array(
                'is_seller' => false                
            );    
        }
        return $data;    
    }
    
    /**
    * WC Marketplace plugin vendor Short info for product listing page
    */    
    public function get_wc_marketplace_vender_short_info($author_id,$product_id){
        
        $html = '';$data = array();
        $vendor = get_wcmp_vendor($author_id);
        if ($vendor) {
            
            $term_vendor = wp_get_post_terms($product_id, 'dc_vendor_shop');
            $contact_seller = $this->pgs_woo_api_contact_seller_status('wc_marketplace');
            
            if (!is_wp_error($term_vendor) && !empty($term_vendor)) {
                
                $rating_result_array = wcmp_get_vendor_review_info($term_vendor[0]->term_id);
                $policies_info = $this->get_wc_marketplace_policies_info($author_id,$product_id);
                
                $store_tnc = (isset($policies_info) && !empty($policies_info))?$policies_info:'';
                $seller_rating = array(
                    "rating" => $rating_result_array['avg_rating'],
                    "count" => $rating_result_array['total_rating']
                );
                
                $data = array(
                    'is_seller' => true,
                    'seller_id' => $author_id,
                    'store_name' => $vendor->user_data->display_name,
                    'address' => '',
                    'seller_address' => '',
                    'seller_rating' => $seller_rating,
                    'store_tnc' => $store_tnc,
                    'contact_seller' => $contact_seller 
                );
                
            }            
        } else {
            $data = array(
                'is_seller' => false                
            );    
        }
        
        return $data;
    }
    
    
    /**
    * Get wc marketplace policies info
    */    
    public function get_wc_marketplace_policies_info($author_id,$product_id){
        
        $wcmp_policy_settings = get_option("wcmp_general_policies_settings_name");
        
        
        $cancellation_policy_product = '';
        $cancellation_policy_user = '';
        $refund_policy_product = '';
        $refund_policy_user = '';
        $shipping_policy_product = '';
        $shipping_policy_user = '';
        $cancellation_policy = isset($wcmp_policy_settings['cancellation_policy']) ? $wcmp_policy_settings['cancellation_policy'] : '';
        $refund_policy = isset($wcmp_policy_settings['refund_policy']) ? $wcmp_policy_settings['refund_policy'] : '';
        $shipping_policy = isset($wcmp_policy_settings['shipping_policy']) ? $wcmp_policy_settings['shipping_policy'] : '';
        $cancellation_policy_label = isset($wcmp_policy_settings['cancellation_policy_label']) ? $wcmp_policy_settings['cancellation_policy_label'] :  __('Cancellation/Return/Exchange Policy','dc-woocommerce-multi-vendor');
        $refund_policy_label = isset($wcmp_policy_settings['refund_policy_label']) ? $wcmp_policy_settings['refund_policy_label'] :  __('Refund Policy','dc-woocommerce-multi-vendor');
        $shipping_policy_label = isset($wcmp_policy_settings['shipping_policy_label']) ? $wcmp_policy_settings['shipping_policy_label'] :  __('Shipping Policy','dc-woocommerce-multi-vendor');
        
        
        if(isset($wcmp_policy_settings['can_vendor_edit_cancellation_policy'])){
        	if(isset($wcmp_policy_settings['is_cancellation_product_level_on'])){		
        		$cancellation_policy_product = get_post_meta($product_id, '_wcmp_cancallation_policy', true);		
        	}	
        	$cancellation_policy_user = get_user_meta($author_id, '_vendor_cancellation_policy', true);
        	
        }
        else {
        	if(isset($wcmp_policy_settings['is_cancellation_product_level_on'])){
        		$cancellation_policy_product = get_post_meta($product_id, '_wcmp_cancallation_policy', true);		
        	}	
        }
        if(isset($wcmp_policy_settings['can_vendor_edit_refund_policy'])){
        	if(isset($wcmp_policy_settings['is_refund_product_level_on'])){
        		$refund_policy_product = get_post_meta($product_id, '_wcmp_refund_policy', true);		
        	}	
        	$refund_policy_user = get_user_meta($author_id, '_vendor_refund_policy', true);
        	
        }
        else {
        	if(isset($wcmp_policy_settings['is_refund_product_level_on'])){
        		$refund_policy_product = get_post_meta($product_id, '_wcmp_refund_policy', true);		
        	}	
        }
        
        if(isset($wcmp_policy_settings['can_vendor_edit_shipping_policy'])){
        	if(isset($wcmp_policy_settings['is_shipping_product_level_on'])){
        		$shipping_policy_product = get_post_meta($product_id, '_wcmp_shipping_policy', true);		
        	}
        	
        	$shipping_policy_user = get_user_meta($author_id, '_vendor_shipping_policy', true);
        	
        }
        else {
        	if(isset($wcmp_policy_settings['is_shipping_product_level_on'])){
        		$shipping_policy_product = get_post_meta($product_id, '_wcmp_shipping_policy', true);		
        	}	
        }
        if(!empty($cancellation_policy_product)) {
        	$cancellation_policy = $cancellation_policy_product;
        }
        else if(!empty($cancellation_policy_user)) {
        	$cancellation_policy = $cancellation_policy_user;
        }
        
        if(!empty($refund_policy_product)) {
        	$refund_policy = $refund_policy_product;
        }
        else if(!empty($refund_policy_user)) {
        	$refund_policy = $refund_policy_user;
        }
        
        if(!empty($shipping_policy_product)) {
        	$shipping_policy = $shipping_policy_product;
        }
        else if(!empty($shipping_policy_user)) {
        	$shipping_policy = $shipping_policy_user;
        }
        
        $html = '';
        if(!empty($cancellation_policy) && !empty($cancellation_policy_label) && isset($wcmp_policy_settings['is_cancellation_on']) ) {            
            
        	$html .= '<h2 class="wcmp_policies_heading">'.$cancellation_policy_label.'</h2>';
        	$html .= '<div class="wcmp_policies_description" >'.$cancellation_policy.'</div>';
        }
        if(!empty($refund_policy) && !empty($refund_policy_label) && isset($wcmp_policy_settings['is_refund_on']) ) { 
        	$html .= '<h2 class="wcmp_policies_heading">'.$refund_policy_label.'</h2>';
        	$html .= '<div class="wcmp_policies_description">'.$refund_policy.'</div>';
        }
        if(!empty($shipping_policy) && !empty($shipping_policy_label) && isset($wcmp_policy_settings['is_shipping_on']) ) {
        	$html .= '<h2 class="wcmp_policies_heading">'.$shipping_policy_label.'</h2>';
        	$html .= '<div class="wcmp_policies_description">'.$shipping_policy.'</div>';
        }
        return $html; 
    }
    
    /**
     * Check contact seller status for active or not
     */ 
    public function pgs_woo_api_contact_seller_status($seller_for){
        $contact_seller = false;
        if( $seller_for == 'dokan'){
            if( dokan_get_option( 'contact_seller', 'dokan_general', 'on' ) == 'on' ) {
                $contact_seller = true;    
            }    
        } else {            
            $capability_settings = get_option('wcmp_general_customer_support_details_settings_name');
            if( isset( $capability_settings['can_vendor_add_customer_support_details'] ) ) {    			 
				$vendor_meta = get_user_meta( $vendor_id );    				
				if( isset($vendor_meta['_vendor_customer_email'][0])) {                        
                    if(isset($vendor_meta['_vendor_customer_email'][0])) { 
                        $to_email = $vendor_meta['_vendor_customer_email'][0];
                    }                    
				}    			
    		} else {
                if(isset($capability_settings['csd_email'])) {
                    $to_email = $capability_settings['csd_email']; 					
                }			
    		}
            if( isset($to_email) && !empty($to_email) ){
                $contact_seller = true;    
            }
        }
        return $contact_seller;
    }
    
    public function get_upload_image_data($image_data,$user_id){
        
        $img = strtotime(date('Ymdhis'));        
        $file_name = $image_data['name'];            
        $ext = pathinfo($file_name,PATHINFO_EXTENSION);            
        $type = array("jpge","jpg","png");        
        if(in_array($ext,$type)) {
            $destination = trailingslashit( PGS_API_PATH . 'img/profile_img' ) . 'user_'.$user_id.'_'.$img.'.'.$ext;
            file_put_contents($destination,base64_decode($image_data['data']));
            $img_url = trailingslashit( PGS_API_URL . 'img/profile_img' ) . 'user_'.$user_id.'_'.$img.'.'.$ext;
            update_user_meta( $user_id, 'pgs_user_image', $img_url );
            return true; 
        } else {
            return true;    
        }         
    }
 }