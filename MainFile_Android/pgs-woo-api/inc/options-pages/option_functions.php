<?php
/**
 * Save option page data  
 */    
if( isset($_POST['submit-api']) ){
        
    if(isset($_POST['pgs']['main_category'])){        
        foreach($_POST['pgs']['main_category'] as $k => $v){
            if ( isset( $v['main_cat_id'] ) && !empty($v['main_cat_id']) ) {                		
        		$product_app_cat_thumbnail_id = isset( $v['product_app_cat_thumbnail_id'] ) ? $v['product_app_cat_thumbnail_id'] : '';                    
                update_term_meta($v['main_cat_id'], 'product_app_cat_thumbnail_id', $product_app_cat_thumbnail_id);            	
            }
        }
    }
    
    if(isset($_POST['pgs']['main_slider'])){
        $t = 0;
        foreach($_POST['pgs']['main_slider'] as $k => $v){                    
            if(isset($v['upload_image_id']) && !empty($v['upload_image_id']) ){
                $vsrc = wp_get_attachment_image_src($v['upload_image_id'], 'large' );                    
                if(!empty($vsrc)){                            
                    $_POST['pgs']['main_slider'][$t]['upload_image_url'] = esc_url($vsrc[0]);                            
                } else {
                    $_POST['pgs']['main_slider'][$t]['upload_image_url'] = ''; 
                }
            }                
            $t++;                                            
        }                   
    }
    
    if(isset($_POST['pgs']['category_banners'])){
        $p = 0;
        foreach($_POST['pgs']['category_banners'] as $k => $v){                    
            if(isset($v['cat_banners_image_id']) && !empty($v['cat_banners_image_id']) ){
                $vsrc = wp_get_attachment_image_src($v['cat_banners_image_id'], 'app_thumbnail' );                    
                if(!empty($vsrc)){                            
                    $_POST['pgs']['category_banners'][$p]['cat_banners_image_url'] = esc_url($vsrc[0]);                            
                } else {
                    $_POST['pgs']['category_banners'][$p]['cat_banners_image_url'] = ''; 
                }
            }
            
            if(isset($v['cat_banners_title']) && !empty($v['cat_banners_title']) ){                                                
                $_POST['pgs']['category_banners'][$p]['cat_banners_title'] = stripslashes($v['cat_banners_title']);                    
            }
            
            $p++;                                            
        }                   
    }
    
    if(isset($_POST['pgs']['banner_ad'])){
        $p = 0;
        foreach($_POST['pgs']['banner_ad'] as $k => $v){                    
            if(isset($v['banner_ad_image_id']) && !empty($v['banner_ad_image_id']) ){
                $banner_ad_image_id = $v['banner_ad_image_id'];                     
                $vsrc = wp_get_attachment_image_src($banner_ad_image_id, 'large' );
                if(!empty($vsrc)){                            
                    $_POST['pgs']['banner_ad'][$p]['banner_ad_image_url'] = $vsrc[0];                            
                } else {
                    $_POST['pgs']['banner_ad'][$p]['banner_ad_image_url'] = ''; 
                }
            }
            $p++;
        }
    }
    
    if(isset($_POST['pgs']['feature_box_heading'])){
        $_POST['pgs']['feature_box_heading'] = stripslashes($_POST['pgs']['feature_box_heading']);                        
    }        
    if(isset($_POST['pgs']['feature_box'])){
        $p = 0;        
        foreach($_POST['pgs']['feature_box'] as $k => $v){                    
            if(isset($v['feature_title']) && !empty($v['feature_title']) ){                                                
                $_POST['pgs']['feature_box'][$p]['feature_title'] = stripslashes($v['feature_title']);
            }
            if(isset($v['feature_content']) && !empty($v['feature_content']) ){                                                
                $_POST['pgs']['feature_box'][$p]['feature_content'] = stripslashes($v['feature_content']);
            }                
            $p++;
        }
    }
    
    if(isset($_POST['pgs_checkout_page'])){
        update_option('pgs_checkout_page', $_POST['pgs_checkout_page']);
    }
    
    update_option('pgs_woo_api_home_option',$_POST['pgs']);
    
    
    
    
    $app_assets = array();
    
    /**
     * App color option for home api 
     */
    $app_assets['app_assets']['app_color']['primary_color'] = '#60A727';
    $app_assets['app_assets']['app_color']['secondary_color'] = '';
    if(isset($_POST['pgs_app_assets']['app_color'])){
        $app_assets['app_assets']['app_color'] = $_POST['pgs_app_assets']['app_color'];
    }               
    update_option( 'pgs_woo_api_app_assets_options',$app_assets );
    
    $message = esc_html__( 'Settings saved.', 'pgs-woo-api' );
    echo pgs_woo_api_admin_notice_render($message,'success');
}

/**
 * Use for upload pen file for notification
 */
function pgs_pem_upload($file_name,$source){
            
    $ext = pathinfo($file_name,PATHINFO_EXTENSION);                                    
    $responce = array();
    if($ext == "pem") {                    
        $destination = trailingslashit( PGS_API_PATH . 'inc/options-pages/pem' ) . $file_name;            
        if (move_uploaded_file( $source, $destination )) {
            $responce = array(
                'status' => 'success',
                'message' => esc_html__( "The file ",'pgs-woo-api' ). basename( $file_name). esc_html__( " has been uploaded.",'pgs-woo-api' )
            );
            
        } else {
            $responce = array(
                'status' => 'error',
                'message' => esc_html__("Sorry, there was an error uploading your file.",'pgs-woo-api' )
            );                 
        }                                                                                      
    } else {
        $responce = array(
            'status' => 'error',
            'message' => esc_html__("Sorry, there was an error uploading your file.",'pgs-woo-api')
        );                            
    }
    return $responce;    
}    
/**
 * Update Setting page daga
 */        
if( isset($_POST['submit-api-auth']) ){        
    foreach($_POST as $key => $val ){
        
        if($key == "pgs_auth"){                
            $pgs_auth['pgs_auth']['client_key'] = sanitize_text_field($val['client_key']);
            $pgs_auth['pgs_auth']['client_secret'] = sanitize_text_field($val['client_secret']);
            $token = (isset($val['token']))?$val['token']:'';
            $token_secret = (isset($val['token_secret']))?$val['token_secret']:'';                
            $pgs_auth['pgs_auth']['token'] = sanitize_text_field($token);
            $pgs_auth['pgs_auth']['token_secret'] = sanitize_text_field($token_secret);    
        }
        
        if($key == "woo_auth"){
            $pgs_auth['woo_auth']['client_key'] = sanitize_text_field($val['client_key']);
            $pgs_auth['woo_auth']['client_secret'] = sanitize_text_field($val['client_secret']);    
        }
        
        if($key == "google_keys"){                
            $google_keys['google_keys']['google_map_api_key'] = sanitize_text_field($val['google_map_api_key']);    
            update_option('pgs_google_keys',$google_keys);
        }
        
        
        if($key == "push_mode"){                
            update_option('pgs_push_mode', $val);    
        }
        
        if($key == "push_status"){                
            update_option('pgs_push_status', $val);    
        }
        
        if($key == "pgs_not_code"){                
            update_option('pgs_not_code', $val);    
        }
        
        if($key == "android_l_s_key"){                
            update_option('android_l_s_key', $val);    
        }
        
        if($key == "pgs_ios_app_url"){                
            update_option('pgs_ios_app_url', $val);    
        }
        
        if($key == "active_vendor"){                
            update_option('pgs_active_vendor', $val);    
        }
        
        if($key == "pem_file_dev_pass"){                
            update_option('pem_file_dev_pass', $val);    
        }
        
        if($key == "pem_file_pro_pass"){                
            update_option('pem_file_pro_pass', $val);    
        }            
    }
    
    
    if(isset($_FILES["pem_file_dev"]["name"]) && !empty($_FILES["pem_file_dev"]["name"])){            
        $resutl = pgs_pem_upload($_FILES["pem_file_dev"]["name"],$_FILES["pem_file_dev"]["tmp_name"]);            
        if( $resutl['status'] == 'success' ){                
            $message = $resutl['message'];
            $status = $resutl['status'];                
            echo pgs_woo_api_admin_notice_render($message,$status);                
            update_option('pem_file_dev',$_FILES["pem_file_dev"]["name"]);    
        } else {
            $message = $resutl['message'];
            $status = $resutl['status'];
            echo pgs_woo_api_admin_notice_render($message,$status);                        
        }            
    }
    
    if(isset($_FILES["pem_file_pro"]["name"]) && !empty($_FILES["pem_file_pro"]["name"])){            
        $resutl = pgs_pem_upload($_FILES["pem_file_pro"]["name"],$_FILES["pem_file_pro"]["tmp_name"]);
        if( $resutl['status'] == 'success' ){
            $message = $resutl['message'];
            $status = $resutl['status'];
            echo pgs_woo_api_admin_notice_render($message,$status);                
            update_option('pem_file_pro',$_FILES["pem_file_pro"]["name"]);   
        } else {
            $message = $resutl['message'];
            $status = $resutl['status'];
            echo pgs_woo_api_admin_notice_render($message,$status);                                            
        }            
    }                        
    update_option('app_auth',$pgs_auth);
    $message = esc_html__( 'Settings saved.', 'pgs-woo-api' );
    echo pgs_woo_api_admin_notice_render($message,'success');
}


function pgs_woo_api_get_app_cat_icon_url($id='',$echo=true){    
    if(empty($id)){
        return false;
    }
    $vsrc = array();$app_cat='';                                            
	$product_app_cat_thumbnail_id = get_term_meta($id, 'product_app_cat_thumbnail_id', true);                                                
    if(isset($product_app_cat_thumbnail_id) && !empty($product_app_cat_thumbnail_id)){
        $vsrc = wp_get_attachment_image_src($product_app_cat_thumbnail_id, 'thumbnail' );
        if(!empty($vsrc)){
            if(!$echo){
                return esc_url($vsrc[0]);    
            } else {
                echo esc_url($vsrc[0]);
            }        
        }    
    }
        
}

function pgs_woo_api_get_app_cat_icon_id($id='',$echo=true){    
    if(empty($id)){
        return false;
    }
    $vsrc = array();$app_cat='';                                            
	$product_app_cat_thumbnail_id = get_term_meta($id, 'product_app_cat_thumbnail_id', true);                                                
    if(isset($product_app_cat_thumbnail_id) && !empty($product_app_cat_thumbnail_id)){
        if(!$echo){
            return $product_app_cat_thumbnail_id;    
        } else {
            echo $product_app_cat_thumbnail_id;
        }    
    }        
}    