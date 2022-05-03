<?php
if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

class PGS_WOO_API_HomeController extends PGS_WOO_API_Controller{
	/**
	 * Endpoint namespace.
	 *
	 * @var string
	 */
	protected $namespace = 'pgs-woo-api/v1';

	/**
	 * Route base.
	 *
	 * @var string
	 */
	protected $rest_base = 'home';
    	
	public function __construct() {
		$this->register_routes();	
	}
	public function register_routes() {
		
		add_action( 'rest_api_init', array( $this, 'pgs_woo_api_register_route'));
	}
	
	
	public function pgs_woo_api_register_route() {
        
        register_rest_route( $this->namespace, $this->rest_base, array(
    		'methods' => WP_REST_Server::CREATABLE,//'POST',
    		'callback' => array($this, 'pgs_woo_api_app_home'),
            'permission_callback' => array($this, 'pgs_woo_api_permission_callback'),
    	) );           
    }
    /**
    * URL : http://yourdomain.com/wp-json/pgs-woo-api/v1/home    
    */   
    public function pgs_woo_api_app_home( WP_REST_Request $request){    
        
        $input = file_get_contents("php://input");
        $request = json_decode($input,true);
        
        $pgs_woo_api_home_option = array();$pgs_woo_api_home_option['app_logo'] = '';$pgs_woo_api_home_option['app_logo_light'] = '';        
        $pgs_woo_api_home_option = get_option('pgs_woo_api_home_option');
        $main_slider_arr = array();
        if(isset($pgs_woo_api_home_option['main_slider']) && !empty($pgs_woo_api_home_option['main_slider'])){
            $t = 0;
            foreach($pgs_woo_api_home_option['main_slider'] as $k => $v){                    
                if(isset($v['upload_image_id']) && !empty($v['upload_image_id']) ){
                    
                    $main_slider_arr[$t]['upload_image_id'] = $v['upload_image_id']; 
                    $main_slider_arr[$t]['slider_cat_id'] = $v['slider_cat_id'];
                    $vsrc = wp_get_attachment_image_src($v['upload_image_id'], apply_filters( 'pgs_woo_api_slider_image', 'large' ));                    
                    if(!empty($vsrc)){                            
                        $main_slider_arr[$t]['upload_image_url'] = esc_url($vsrc[0]);                            
                    } else {
                        $main_slider_arr[$t]['upload_image_url'] = ''; 
                    }
                    $t++;
                }
                                                            
            }
        }
        $pgs_woo_api_home_option['main_slider'] = $main_slider_arr;
         
        $main_category_arr = array();
        if(isset($pgs_woo_api_home_option['main_category']) && !empty($pgs_woo_api_home_option['main_category'])){        
            $p = 0;            
            foreach($pgs_woo_api_home_option['main_category'] as $key => $val){
                
                if(isset($val['main_cat_id']) && !empty($val['main_cat_id']) ){                    
                    $cat_data = get_term_by( 'id',$val['main_cat_id'],'product_cat' );
                    
                    $main_category_arr[$p]['main_cat_id'] = $val['main_cat_id'];
                    $main_category_arr[$p]['main_cat_name'] = html_entity_decode($cat_data->name);
                    
                    $attch_id = get_term_meta( $val['main_cat_id'], 'product_app_cat_thumbnail_id', true );                 
                    $vsrc = wp_get_attachment_image_src($attch_id, apply_filters( 'pgs_woo_api_main_category_image', 'thumbnail' ) );                                
                    if(!empty($vsrc)){                    
                        $main_category_arr[$p]['main_cat_image'] = $vsrc[0];                                                
                    } else {
                        $main_category_arr[$p]['main_cat_image'] = ''; 
                    }                
                    $p++;
                }                
            }            
        }
        $pgs_woo_api_home_option['main_category'] = $main_category_arr;
        
        $category_banners_arr = array();
        if(isset($pgs_woo_api_home_option['category_banners']) && !empty($pgs_woo_api_home_option['category_banners'])){
            $p = 0;            
            foreach($pgs_woo_api_home_option['category_banners'] as $k => $v){                    
                if( !empty($v['cat_banners_image_id']) || !empty($v['cat_banners_title']) || !empty($v['cat_banners_cat_id']) ){
                    if( !empty($v['cat_banners_image_id']) ){                    
                        $category_banners_arr[$p]['cat_banners_image_id'] = $v['cat_banners_image_id'];                        
                        $vsrc = wp_get_attachment_image_src($v['cat_banners_image_id'], apply_filters( 'pgs_woo_api_cat_banners_image', 'app_thumbnail' ));
                        if(!empty($vsrc)){                            
                            $category_banners_arr[$p]['cat_banners_image_url'] = esc_url($vsrc[0]);                            
                        } else {
                            $category_banners_arr[$p]['cat_banners_image_url'] = ''; 
                        }
                    }
                    $category_banners_arr[$p]['cat_banners_cat_id'] = $v['cat_banners_cat_id'];
                    if( !empty($v['cat_banners_title']) ){                                                
                        $category_banners_arr[$p]['cat_banners_title'] = stripslashes($v['cat_banners_title']);                    
                    } else {
                        $category_banners_arr[$p]['cat_banners_title'] = '';
                    }                                          
                }
                $p++;                                                           
            }                   
        }        
        $pgs_woo_api_home_option['category_banners'] = $category_banners_arr;
        
        $banner_ad_arr = array();
        if(isset($pgs_woo_api_home_option['banner_ad']) && !empty($pgs_woo_api_home_option['banner_ad'])){
            $b = 0;
            foreach($pgs_woo_api_home_option['banner_ad'] as $k => $v){                    
                if(isset($v['banner_ad_image_id']) && !empty($v['banner_ad_image_id']) ){
                    $banner_ad_image_id = $v['banner_ad_image_id'];                     
                    $vsrc = wp_get_attachment_image_src($banner_ad_image_id, apply_filters( 'pgs_woo_api_banner_ad_image', 'large' ) );
                    if(!empty($vsrc)){                            
                        $banner_ad_arr[$b]['banner_ad_image_url'] = $vsrc[0];                            
                    } else {
                        $banner_ad_arr[$b]['banner_ad_image_url'] = ''; 
                    }
                    $banner_ad_arr[$b]['banner_ad_image_id'] = $banner_ad_image_id;
                
                    $banner_ad_arr[$b]['banner_ad_cat_id'] = $v['banner_ad_cat_id'];
                    $b++;
                }
            }
        }
        $pgs_woo_api_home_option['banner_ad'] = $banner_ad_arr; 
        
        $price_formate_option = get_woo_price_formate_option_array();        
        $feature_box_status = (isset($pgs_woo_api_home_option['feature_box_status']) && !empty($pgs_woo_api_home_option['feature_box_status']))?$pgs_woo_api_home_option['feature_box_status']:'enable';
        if($feature_box_status == "enable"){
            $f = 0;
            if(isset($pgs_woo_api_home_option['feature_box'])&& !empty($pgs_woo_api_home_option['feature_box'])){
                foreach($pgs_woo_api_home_option['feature_box'] as $key => $val){
                    
                    if(isset($val['feature_image_id']) && !empty($val['feature_image_id']) ){
            
                        $attch_id = $val['feature_image_id'];                 
                        $vsrc = wp_get_attachment_image_src($attch_id, apply_filters( 'pgs_woo_api_feature_image', 'thumbnail' ) );                                
                        if(!empty($vsrc)){                    
                            $pgs_woo_api_home_option['feature_box'][$f]['feature_image'] = $vsrc[0];                                                
                        } else {
                            $pgs_woo_api_home_option['feature_box'][$f]['feature_image'] = ''; 
                        }                
                    }                
                    $f++;
                }
            }                
            
        } else {
            $pgs_woo_api_home_option['feature_box'] = array();
        }
        $pgs_woo_api_home_option['all_categories'] = $this->pgs_woo_api_cat_list();        
        $pgs_woo_api_home_option['popular_products'] = $this->pgs_woo_api_get_popular_products();
        $pgs_woo_api_home_option['scheduled_sale_products'] = $this->pgs_woo_api_scheduled_sale_products();
        $pgs_woo_api_home_option['is_wishlist_active'] = pgs_woo_api_is_wishlist_active();
        $pgs_woo_api_home_option['is_currency_switcher_active'] = pgs_woo_api_is_currency_switcher_active();
        $pgs_woo_api_home_option['is_order_tracking_active'] = pgs_woo_api_is_order_tracking_active();
        $pgs_woo_api_home_option['is_reward_points_active'] = pgs_woo_api_is_reward_points_active();
        $pgs_woo_api_home_option['is_abandoned_cart_active'] = pgs_woo_api_is_abandoned_cart();
        $pgs_woo_api_home_option['price_formate_options'] = $price_formate_option;
                
        $pgsiosappurl = get_option('pgs_ios_app_url');
        $pgs_ios_app_url = (isset($pgsiosappurl))?$pgsiosappurl:'';
        $pgs_woo_api_home_option['ios_app_url'] = $pgs_ios_app_url;
        $site_language = get_bloginfo('language');
        $pgs_woo_api_home_option['site_language'] = $site_language;            	    
    	
        if(!empty($pgs_woo_api_home_option['app_logo'])){    	  
            $src = wp_get_attachment_image_src($pgs_woo_api_home_option['app_logo'], apply_filters( 'pgs_woo_api_app_logo_image', 'full' ) );
            if(!empty($src)){
                $pgs_woo_api_home_option['app_logo'] = $src[0];
            }   
    	}
        
        if(!empty($pgs_woo_api_home_option['app_logo_light'])){
            $src = wp_get_attachment_image_src($pgs_woo_api_home_option['app_logo_light'], apply_filters( 'pgs_woo_api_app_logo_light_image', 'full' ) );
            if(!empty($src)){
                $pgs_woo_api_home_option['app_logo_light'] = $src[0];
            }
        }
        
        /**
         *  Get App Assets app color
         */ 
        $app_color = array( 
            'header_color' => '',
            'primary_color' => '#60A727',
            'secondary_color' => ''
        );
        $app_assets = get_option('pgs_woo_api_app_assets_options');
        if(isset($app_assets) && !empty($app_assets)){
            if(isset($app_assets['app_assets']['app_color']) && !empty($app_assets['app_assets']['app_color'])){                
                $app_color = $app_assets['app_assets']['app_color'];
            }
        }
        $pgs_woo_api_home_option['app_color'] = $app_color;
        
        $is_rtl = false;
        if ( is_rtl() ) {
            $is_rtl = true;
        }
        $pgs_woo_api_home_option['is_rtl'] = $is_rtl;
        $cs_status = pgs_woo_api_is_currency_switcher_active();
        if($cs_status){
            $currency_data = get_option('woocs');
            if(isset($currency_data) && !empty($currency_data)){
                global $WOOCS;
                $currencies = $WOOCS->get_currencies();
                if(isset($currencies) && !empty($currencies)){
                    $pgs_woo_api_home_option['currency_switcher'] = $currencies;                        
                }    
            }
        }
        return $pgs_woo_api_home_option;
    }
    
    public function pgs_woo_api_cat_list(){
        $taxonomy     = 'product_cat';
        $orderby      = 'name';  
        $show_count   = 1;      // 1 for yes, 0 for no
        $pad_counts   = 1;      // 1 for yes, 0 for no
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
            
        $data = array();
        if(isset($all_categories) && !empty($all_categories)){
            foreach ($all_categories as $cat) {
                
                $product_app_cat_thumbnail_id = get_term_meta($cat->term_id, 'product_app_cat_thumbnail_id', true); 
                $vsrc = wp_get_attachment_image_src($product_app_cat_thumbnail_id, apply_filters( 'pgs_woo_api_app_cat_thumbnail_image', 'thumbnail' ) );
                if(!empty($vsrc)){                            
                    $main_cat_id_image = $vsrc[0];                            
                } else {
                    $main_cat_id_image = ''; 
                }
                
                $data[] = array(
                    'description' => $cat->category_description,            
                    'id' => $cat->term_id,
                    'image' => array(                
                        'src' => $main_cat_id_image,                
                    ),        
                    'name' => html_entity_decode($cat->name),
                    'parent' => $cat->category_parent,
                    'slug' => $cat->slug,
                );       
            }
        }
        return $data;    
    }    
 }
new PGS_WOO_API_HomeController;