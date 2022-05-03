<?php
/**
 * Add Home Api  
 */
add_action('admin_menu', 'pgs_woo_api_option_page_menu');
function pgs_woo_api_option_page_menu(){	
	add_menu_page( esc_html('PGS Woo API','pgs-woo-api'), esc_html('App Settings','pgs-woo-api'), 'manage_options', 'pgs-woo-api-settings', 'pgs_woo_api_callback');
}

function pgs_woo_api_callback(){    
    
    require_once( PGS_API_PATH . 'inc/options-pages/option_functions.php' );
    $pgs_woo_api_home_option = array();
    $pgs_woo_api_home_option = get_option('pgs_woo_api_home_option');
    
    //get app color options
    $app_assets_options = get_option('pgs_woo_api_app_assets_options');
    
    $primary_color = (isset($app_assets_options['app_assets']['app_color']['primary_color']))?$app_assets_options['app_assets']['app_color']['primary_color']:'';
    $secondary_color = (isset($app_assets_options['app_assets']['app_color']['secondary_color']))?$app_assets_options['app_assets']['app_color']['secondary_color']:'';
    $header_color = (isset($app_assets_options['app_assets']['app_color']['header_color']))?$app_assets_options['app_assets']['app_color']['header_color']:'';
    ?>
    <div class="wrap">
		<h2></h2>
        <div class="wrap-top gradient-bg">
            <h2 class="wp-heading-inline"><?php esc_html_e('App Settings','pgs-woo-api')?></h2>
            <div class="pgs-woo-api-right">
              <!-- <div class="pgs-woo-api-right-heading"><?php /*  esc_html_e('Publish','pgs-woo-api')*/?></div>  -->
              <div class="publish-btn-box">
                  <span class="spinner"></span>
                  <button id="publish-btn" type="submit" name="submit-api" form="pgs-woo-api-form" class="pgs-woo-api-btn button button-primary Submit-btn" value="Submit"><?php esc_html_e('Save Changes','pgs-woo-api')?></button>
              </div>
              <div class="mobile-screen-view"></div>            
            </div>
        </div>

        <form action="" method="post" name="pgs-woo-api-form" id="pgs-woo-api-form">
            <div id="pgs-woo-api-tabs">
                <ul>
                    <li><a href="#pgs-woo-api-tabs-app-primary-logo"><?php esc_html_e('Primary Logo','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-app-secondary-logo"><?php esc_html_e('Secondary Logo','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-app-color"><?php esc_html_e('App Color','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-main-category-menu"><?php esc_html_e('Main Category Menu','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-home-slider"><?php esc_html_e('Home Slider Banner','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-category-banners"><?php esc_html_e('Category Banners','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-banner-ads"><?php esc_html_e('Banner Ads','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-feature-box"><?php esc_html_e('Feature Box','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-app-pages"><?php esc_html_e('App Pages','pgs-woo-api')?></a></li>                    
                    <li><a href="#pgs-woo-api-tabs-checkout-page"><?php esc_html_e('Checkout Page','pgs-woo-api')?></a></li>                    
                    <li><a href="#pgs-woo-api-tabs-social-links"><?php esc_html_e('Social Links','pgs-woo-api')?></a></li>
                    <li><a href="#pgs-woo-api-tabs-contact-info"><?php esc_html_e('Contact Info','pgs-woo-api')?></a></li>
                </ul>
                
                <div id="pgs-woo-api-tabs-app-primary-logo">
            
                    <div class="pgs-woo-api-panel" id="pgs-woo-api-app_logo">
                        <div class="pgs-woo-api-panel-body">
                        <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Primary Logo','pgs-woo-api')?></div>  
							<p class="description"><?php esc_html_e('Logo display on the header of the app.','pgs-woo-api')?></p>
                            <?php $pgs_app_logo = $pgs_woo_api_home_option;?>
                            <p class="note-desc"><strong><?php esc_html_e('IMPORTANT','pgs-woo-api')?>:</strong><br />
								<span class="description"><?php esc_html_e('For best performance and image quality in the mobile application, strictly follow the below standards for logo images:','pgs-woo-api')?>
								</span><br />
							<?php esc_html_e('Dimension: ', 'pgs-woo-api')?> <strong><?php esc_html_e('164 x 51', 'pgs-woo-api')?></strong><br />
							<?php esc_html_e('File Type: ', 'pgs-woo-api')?> <strong><?php esc_html_e('PNG', 'pgs-woo-api')?></strong>
							</p>
                            <div class="pgs-woo-api-field-group">
                                <div class="pgs-woo-api-form-group">
                                    <?php                                        
                                    if(isset($pgs_app_logo['app_logo_light']) && !empty($pgs_app_logo['app_logo_light']) ){
                                        $src = wp_get_attachment_image_src($pgs_app_logo['app_logo_light'], 'medium' );
                                        if(!empty($src)){
                                            ?>
                                            <div class="upload_image">
                                                <img src="<?php echo esc_url($src[0])?>" alt="No image" />
                                            </div>
                                            <input type="hidden" name="pgs[app_logo_light]" id="pgs[app_logo_light]" class="upload_image_id" value="<?php echo $pgs_app_logo['app_logo_light'];?>" />  
                                            <a href="javascript:void(0);" class="upload-image-button button button-default" id="pgs-slider-app_logo_light"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                            <?php
                                        }
                                    } else {?>                                                        
                                        <div class="upload_image"></div>
                                        <input type="hidden" name="pgs[app_logo_light]" id="pgs[app_logo_light]" class="upload_image_id" value="" />
                                        <a href="javascript:void(0);" class="upload-image-button button button-primary" id="pgs-slider-app_logo_light"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                        <?php
                                    }?>
                                    <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( ! @$pgs_app_logo['app_logo_light'] ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>                                
                                </div>                                                                            
                            </div>                            
                        </div>
                        <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="primary-logo-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="primary-logo-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="primary-logo-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/logo.png'?>" />
							</div>
							<div id="primary-logo-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/logo.png'?>" />
							</div>
                        </div>
                    </div>                
                </div><!-- #pgs-woo-api-tabs-app-primary-logo -->
				<div id="pgs-woo-api-tabs-app-secondary-logo">
            
                    <div class="pgs-woo-api-panel" id="pgs-woo-api-app_logo">
                        <div class="pgs-woo-api-panel-body">
							<div class="pgs-woo-api-panel-heading">
								<?php esc_html_e('Secondary Logo','pgs-woo-api')?>
							</div>  
							<p class="description"><?php esc_html_e('Logo display on other app pages like About Us and Contact Us.','pgs-woo-api')?></p>
                        
                            <?php $pgs_app_logo = $pgs_woo_api_home_option;?>
                            <p class="note-desc"><strong><?php esc_html_e('IMPORTANT','pgs-woo-api')?>:</strong><br />
								<span class="description"><?php esc_html_e('For best performance and image quality in the mobile application, strictly follow the below standards for logo images:','pgs-woo-api')?>
								</span><br />
							<?php esc_html_e('Dimension: ', 'pgs-woo-api')?> <strong><?php esc_html_e('164 x 51', 'pgs-woo-api')?></strong><br />
							<?php esc_html_e('File Type: ', 'pgs-woo-api')?> <strong><?php esc_html_e('PNG', 'pgs-woo-api')?></strong>
							</p>
                            <div class="pgs-woo-api-field-group">
                                <div class="pgs-woo-api-form-group">
                                    <?php
                                    if(isset($pgs_app_logo['app_logo']) && !empty($pgs_app_logo['app_logo']) ){
                                        $src = wp_get_attachment_image_src($pgs_app_logo['app_logo'], 'medium' );
                                        if(!empty($src)){
                                            ?>
                                            <div class="upload_image">
                                                <img src="<?php echo esc_url($src[0])?>" alt="No image" />
                                            </div>
                                            <input type="hidden" name="pgs[app_logo]" id="pgs[app_logo]" class="upload_image_id" value="<?php echo $pgs_app_logo['app_logo']; ?>" />  
                                            <a href="javascript:void(0);" class="upload-image-button button button-default" id="pgs-slider-app_logo"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                            <?php
                                        }
                                    } else {?>
                                        <div class="upload_image"></div>
                                        <input type="hidden" name="pgs[app_logo]" id="pgs[app_logo]" class="upload_image_id" value="" />
                                        <a href="javascript:void(0);" class="upload-image-button button button-primary" id="pgs-slider-app_logo"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                        <?php
                                    }?>
                                    <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( ! @$pgs_app_logo['app_logo'] ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>                                
                                </div>
                                
                            </div>
                        </div>
                        <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="secondary-logo-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="secondary-logo-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="secondary-logo-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/secondary-logo.png'?>" />
							</div>
							<div id="secondary-logo-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/secondary-logo.png'?>" />
							</div>
                        </div>                    
                    </div>                
                </div><!-- #pgs-woo-api-tabs-app-logo -->
                
                <div id="pgs-woo-api-tabs-app-color">
                    <div class="pgs-woo-api-panel">
                        <div class="pgs-woo-api-panel-body">
                        <div class="pgs-woo-api-panel-heading"><?php esc_html_e('App Color','pgs-woo-api')?></div><p class="description"><?php esc_html_e('You can change app default color and set as per your app design.','pgs-woo-api')?></p>      
                        
                            <div class="pgs-woo-api-form-group">
                                <div class="form-field term-thumbnail-wrap radio-button-inline">
                                    <label><?php esc_html_e('Header Color','pgs-woo-api')?></label>
                                    <div id="">            
                                        <input type="text" id="header_color_code" class="cpa-color-picker" name="pgs_app_assets[app_color][header_color]" value="<?php echo esc_attr($header_color)?>"/>            
                                    </div>                                            
                                </div>                        
                            </div>
                            <div class="pgs-woo-api-form-group">
                                <div class="form-field term-thumbnail-wrap radio-button-inline">
                                    <label><?php esc_html_e('Primary Color','pgs-woo-api')?></label>
                                    <div id="">            
                                        <input type="text" id="primary_color_code" class="cpa-color-picker" name="pgs_app_assets[app_color][primary_color]" value="<?php echo esc_attr($primary_color)?>"/>            
                                    </div>                                            
                                </div>                        
                            </div>
                            <div class="pgs-woo-api-form-group">
                                <div class="form-field term-thumbnail-wrap radio-button-inline">
                                    <label><?php esc_html_e('Secondary Color','pgs-woo-api')?></label>
                                    <div id="">            
                                        <input type="text" id="secondary_color_code" class="cpa-color-picker" name="pgs_app_assets[app_color][secondary_color]" value="<?php echo esc_attr($secondary_color)?>"/>            
                                    </div>                                            
                                </div>                        
                            </div>
                                                   
                        </div>                        
                        <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="color-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="color-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="color-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/color.png'?>" />
							</div>
							<div id="color-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/color.png'?>" />
							</div>
                        </div>                        
                    </div>
                </div><!-- #pgs-woo-api-tabs-app-color -->
                
                <div id="pgs-woo-api-tabs-main-category-menu">
            
                <div class="pgs-woo-api-panel" id="pgs-woo-api-main-category" data-limit="6">
                    <div class="pgs-woo-api-panel-body">
                        <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Main Category Menu','pgs-woo-api')?></div>  
                        <div class="pgs-woo-api-repeater-field-group" data-repeater-list="pgs[main_category]">
                             <p class="description"><?php esc_html_e('Categories menu items display on the home screen.','pgs-woo-api')?></p>
                            <?php
                            $default_array['main_category'] = array(
                                array(
                                    'main_cat_id' => '',                                        
                                )           
                            );
                            $main_category = array();
                            $main_category = $pgs_woo_api_home_option;
                            if(!isset($main_category['main_category']) || empty($main_category['main_category'])){
                                $main_category = $default_array;
                            }
                             
                            if(isset($main_category['main_category']) && !empty($main_category['main_category'])){ 
                                $i = 0;
                                foreach($main_category['main_category'] as $key => $val){                                    
                                    ?>
                                    <div class="pgs-woo-api-field-group" data-repeater-item>
                                        <div class="pgs-woo-api-form-group main-category">
                                            <label><?php esc_html_e('Select category menu item','pgs-woo-api')?></label>                                        
                                            <select class="pgs-woo-api-form-control pgs-woo-api_main_category" name="pgs[main_category][<?php echo $i?>][main_cat_id]">
                                                <option value=""><?php esc_html_e('Select','pgs-woo-api')?></option>
                                                <?php  
                                                $img = '';                                          
                                                $select = pgs_woo_api_get_all_woo_cat();
                                                if(isset($select) && !empty($select)){                                                                
                                                    $selected = '';
                                                    foreach($select as $sekey => $seval){
                                                        $sel = '';
                                                        if($val['main_cat_id'] == $sekey){
                                                            $sel = 'selected="selected"';    
                                                        }
                                                        $caticon_url = 'data-caticon="'.pgs_woo_api_get_app_cat_icon_url($sekey,false).'"';                                                        
                                                        $caticon_id  = ' data-caticonid="'.pgs_woo_api_get_app_cat_icon_id($sekey,false).'"';
                                                        echo '<option value="'.$sekey.'" '.$sel.' '.$caticon_url.$caticon_id.'>'.$seval.'</option>';                                                
                                                    }
                                                }?>
                                            </select>                                    
                                        </div>                                    
                                        <div class="pgs-woo-api-form-group pgs-woo-api_main_category_icon" style="<?php echo ( ! @$val['main_cat_id']  ? 'display:none;' : '' ); ?>">
                                            <label><?php esc_html_e('Category Icon Image','pgs-woo-api')?></label>
                                            <?php $product_app_cat_thumbnail_id = 0;                                        
                                            if(isset($val['main_cat_id']) && !empty($val['main_cat_id']) ){                                            
                                                $vsrc = array();$app_cat='';                                            
                                            	$product_app_cat_thumbnail_id = get_term_meta($val['main_cat_id'], 'product_app_cat_thumbnail_id', true);                                                
                                                $vsrc = wp_get_attachment_image_src($product_app_cat_thumbnail_id, 'thumbnail' );                                            
                                                if(!empty($vsrc)){
                                                    ?>
                                                    <div class="upload_image">
                                                        <img src="<?php echo esc_url($vsrc[0])?>" alt="No image" />
                                                    </div>
                                                    <input type="hidden" name="pgs[main_category][<?php echo $i?>][product_app_cat_thumbnail_id]" id="pgs[product_app_cat_thumbnail_id][<?php echo $i?>]" class="upload_image_id" value="<?php echo $product_app_cat_thumbnail_id?>" />  
                                                    <a href="javascript:void(0);" class="upload-image-button button button-default" id="product-app-cat-thumbnail-id-<?php echo $i?>" data-lbl="<?php esc_html_e( 'Edit','pgs-woo-api')?>"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                                    <?php
                                                } else {?>                                                        
                                                    <div class="upload_image"></div>
                                                    <input type="hidden" name="pgs[main_category][<?php echo $i?>][product_app_cat_thumbnail_id]" id="pgs[product_app_cat_thumbnail_id][<?php echo $i?>]" class="upload_image_id" value="" />
                                                    <a href="javascript:void(0);" class="upload-image-button button button-primary" id="product-app-cat-thumbnail-id-<?php echo $i?>" data-lbl="<?php esc_html_e( 'Add Image','pgs-woo-api')?>"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                                    <?php
                                                }
                                            } else {?>                                                        
                                                <div class="upload_image"></div>
                                                <input type="hidden" name="pgs[main_category][<?php echo $i?>][product_app_cat_thumbnail_id]" id="pgs[product_app_cat_thumbnail_id][<?php echo $i?>]" class="upload_image_id" value="" />
                                                <a href="javascript:void(0);" class="upload-image-button button button-primary" id="product-app-cat-thumbnail-id-<?php echo $i?>" <?php esc_html_e( 'Add Image','pgs-woo-api')?>><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                                <?php
                                            }?>                                                
                                            <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( $product_app_cat_thumbnail_id == 0 ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>                                                
                                        </div>
                                        <span class="removeBanner dashicons dashicons-dismiss" data-repeater-delete><!-- Remove --></span>
                                    </div>
                                    <?php 
                                    $i++;                                                
                                }                                           
                            } ?>
                        </div>
                        <button type="button" class="pgs-woo-api-btn button button-primary" data-repeater-create><?php esc_html_e('Add','pgs-woo-api')?></button>
                    </div>
                        
                    <div class="pgs-woo-api-panel-sidebar">
						<div class="device-select">
							<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="categories-android-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
							</a>
							<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="categories-ios-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
							</a>
						</div>
						<div id="categories-android-section" class="device-display">
							<img src="<?php echo PGS_API_URL.'img/android/categories.png'?>" />
						</div>
						<div id="categories-ios-section" class="device-display hidden">
							<img src="<?php echo PGS_API_URL.'img/ios/categories.png'?>" />
						</div>
					</div> 
                </div>
                
            </div><!-- #pgs-woo-api-tabs-main-category-menu -->
                
                <div id="pgs-woo-api-tabs-home-slider">
                
                    <div class="pgs-woo-api-panel" id="pgs-woo-api-main_slider">
                        <div class="pgs-woo-api-panel-body">
                        <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Home Slider Banner','pgs-woo-api')?></div>  
                        <p class="description"><?php esc_html_e('These banners display as a slider on the home screen.','pgs-woo-api')?></p>
                        <p class="note-desc"><strong><?php esc_html_e('IMPORTANT','pgs-woo-api')?>:</strong><br />
							<span class="description">
							<?php 
							echo wp_kses(
								__('For best performance and image quality of slider banners in the mobile application, strictly use <strong>1125 x 633</strong> (WidthxHeight in px) image for slider banners.','pgs-woo-api'),
								array(
									'strong' => array()
								)
							);
							?>
							</span>
						</p>
						<div class="pgs-woo-api-repeater-field-group" data-repeater-list="pgs[main_slider]">                         
                                <?php                            
                                $default_array['main_slider'] = array(
                                    array(
                                            'upload_image_id' => '',
                                            'slider_cat_id' => ''
                                    )           
                                );
                                $pgs_main_slider = array();
                                $pgs_main_slider = $pgs_woo_api_home_option;
                                if(empty($pgs_main_slider['main_slider'])){
                                    $pgs_main_slider = $default_array;
                                }
                                if(isset($pgs_main_slider['main_slider']) && !empty($pgs_main_slider['main_slider'])){ 
                                    $i = 0;
                                    foreach($pgs_main_slider['main_slider'] as $key => $val){                                    
                                        ?>
                                        <div  class="pgs-woo-api-field-group" id="main-slider-row-<?php echo $i?>" data-repeater-item>
                                            <div class="pgs-woo-api-form-group">
                                                <label><?php esc_html_e( 'Slider Image', 'pgs-woo-api' )?></label>
                                                <?php                                        
                                                if(isset($val['upload_image_id']) && !empty($val['upload_image_id']) ){
                                                    $src = wp_get_attachment_image_src($val['upload_image_id'], 'medium' );
                                                    if(!empty($src)){
                                                        ?>
                                                        <div class="upload_image">
                                                            <img src="<?php echo esc_url($src[0])?>" alt="No image" />
                                                        </div>
                                                        <input type="hidden" name="pgs[main_slider][<?php echo $i?>][upload_image_id]" id="pgs[main_slider][<?php echo $i?>]" class="upload_image_id" value="<?php echo $val['upload_image_id']; ?>" />  
                                                        <a href="javascript:void(0);" class="upload-image-button button button-default" id="pgs-slider-<?php echo $i?>"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                                        <?php
                                                    }
                                                } else {?>                                                        
                                                    <div class="upload_image"></div>
                                                    <input type="hidden" name="pgs[main_slider][<?php echo $i?>][upload_image_id]" id="pgs[main_slider][<?php echo $i?>]" class="upload_image_id" value="" />
                                                    <a href="javascript:void(0);" class="upload-image-button button button-primary" id="pgs-slider-<?php echo $i?>"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                                    <?php
                                                }?>                                                
                                                <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( ! $val['upload_image_id'] ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>                                                
                                            </div>                                            
                                                
                                            <div class="pgs-woo-api-form-group">
                                                <label><?php esc_html_e('Select Product Category','pgs-woo-api')?></label>                                        
                                                <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[main_slider][<?php echo $i?>][slider_cat_id]">
                                                    <option value=""><?php esc_html_e('Select','pgs-woo-api')?></option>
                                                    <?php                                            
                                                    $select = pgs_woo_api_get_all_woo_cat();
                                                    if(isset($select) && !empty($select)){                                                                
                                                        $selected = '';
                                                        foreach($select as $sekey => $seval){
                                                            $sel = '';
                                                            if($val['slider_cat_id'] == $sekey){
                                                                $sel = 'selected="selected"';    
                                                            }
                                                            echo '<option value="'.$sekey.'" '.$sel.'>'.$seval.'</option>';                                                
                                                        }
                                                    }?>
                                                </select>                        
                                            </div>                                        
                                            <span class="remove dashicons dashicons-dismiss" data-repeater-delete><!-- Remove Option --></span>
                                        </div>
                                        <?php 
                                        $i++;                                                
                                    }                                           
                                } ?>
                            
                            </div>            
                            <button type="button" class="pgs-woo-api-btn button button-primary" data-id="<?php echo $i?>" data-repeater-create><?php esc_html_e('Add','pgs-woo-api')?></button>
                        </div>
                        
                        <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="main-banner-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="main-banner-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="main-banner-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/main-banner.png'?>" />
							</div>
							<div id="main-banner-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/main-banner.png'?>" />
							</div>
						</div> 
                    </div>
                
              </div><!-- #pgs-woo-api-tabs-home-slider -->
              <div id="pgs-woo-api-tabs-category-banners">
                
                 <div class="pgs-woo-api-panel" id="pgs-woo-api-category-banners">
                    <div class="pgs-woo-api-panel-body">
                    <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Category Banners','pgs-woo-api')?></div>  
                    <p class="description"><?php esc_html_e( 'Banners displayed on the home screen.','pgs-woo-api' )?></p>
					<p class="note-desc"><strong><?php esc_html_e('IMPORTANT','pgs-woo-api')?>:</strong><br />
						<span class="description">
						<?php 
						echo wp_kses(
							__('For best performance and image quality of category banners in the mobile application, strictly use <strong>420 x 305</strong> (WidthxHeight in px) image for category banners.','pgs-woo-api'),
							array(
								'strong' => array()
							)
						);
						?>
						</span>
					</p>
                   <div class="pgs-woo-api-repeater-field-group" data-repeater-list="pgs[category_banners]">                                    
                        <?php                            
                        $default_array['category_banners'] = array(
                            array(
                                'cat_banners_image_id' => '',
                                'cat_banners_title' => '',
                                'cat_banners_cat_id' => ''
                            )           
                        );
                        $pgs_category_banners = array();
                        $pgs_category_banners = $pgs_woo_api_home_option;
                        if(empty($pgs_category_banners['category_banners'])){
                            $pgs_category_banners = $default_array;
                        } 
                        if(isset($pgs_category_banners['category_banners']) && !empty($pgs_category_banners['category_banners'])){ 
                            $i = 0;
                            foreach($pgs_category_banners['category_banners'] as $key => $val){                                    
                                ?>
                                <div class="pgs-woo-api-field-group" data-repeater-item>
                                    <div class="pgs-woo-api-form-group">
                                        <label><?php esc_html_e( 'Banner Image','pgs-woo-api' )?></label>
                                        <?php                                        
                                        if(isset($val['cat_banners_image_id']) && !empty($val['cat_banners_image_id'])){
                                            $src = wp_get_attachment_image_src($val['cat_banners_image_id'], 'medium' );
                                            if(!empty($src)){
                                                ?>
                                                <div class="upload_image">
                                                    <img src="<?php echo esc_url($src[0])?>" alt="No image" />
                                                </div>
                                                <input type="hidden" name="pgs[category_banners][<?php echo $i?>][cat_banners_image_id]" class="upload_image_id" value="<?php echo $val['cat_banners_image_id']; ?>" />  
                                                <a href="javascript:void(0);" class="upload-image-button button button-default" id="pgs-banner-upload-image-<?php echo $i?>"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                                <?php
                                            }
                                        } else {?>                                                        
                                            <div class="upload_image"></div>
                                            <input type="hidden" name="pgs[category_banners][<?php echo $i?>][cat_banners_image_id]" class="upload_image_id" value="" />
                                            <a href="javascript:void(0);" class="upload-image-button button button-primary" id="pgs-banner-upload-image-<?php echo $i?>"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                            <?php
                                        }?>                                        
                                        <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( !$val['cat_banners_image_id'] ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>                                        
                                    </div>                                            
                                        
                                    
                                    <div class="pgs-woo-api-form-group">
                                        <label><?php esc_html_e('Banner Title','pgs-woo-api')?></label>                                        
                                        <input type="text" name="pgs[category_banners][<?php echo $i?>][cat_banners_title]" class="pgs-woo-api-form-control" value="<?php echo $val['cat_banners_title']; ?>"  />                        
                                    </div>
                                    
                                    
                                    <div class="pgs-woo-api-form-group">
                                        <label><?php esc_html_e('Select Product Category','pgs-woo-api')?></label>                                        
                                        <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[category_banners][<?php echo $i?>][cat_banners_cat_id]">
                                            <option value=""><?php esc_html_e('Select','pgs-woo-api')?></option>
                                            <?php                                            
                                            $select = pgs_woo_api_get_all_woo_cat();
                                            if(isset($select) && !empty($select)){                                                                
                                                $selected = '';
                                                foreach($select as $sekey => $seval){
                                                    $sel = '';
                                                    if($val['cat_banners_cat_id'] == $sekey){
                                                        $sel = 'selected="selected"';    
                                                    }
                                                    echo '<option value="'.$sekey.'" '.$sel.'>'.$seval.'</option>';                                                
                                                }
                                            }?>
                                        </select>                        
                                    </div>                                        
                                    <span class="removeBanner dashicons dashicons-dismiss" data-repeater-delete><!-- Remove --></span>
                                </div>
                                <?php 
                                $i++;                                                
                            }                                           
                        } ?>
                                            
                        </div>            
                        <button type="button" class="pgs-woo-api-btn button button-primary" data-repeater-create><?php esc_html_e('Add','pgs-woo-api')?></button>
                    </div>
                    <div class="pgs-woo-api-panel-sidebar">
						<div class="device-select">
							<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="category-banners-android-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
							</a>
							<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="category-banners-ios-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
							</a>
						</div>
						<div id="category-banners-android-section" class="device-display">
							<img src="<?php echo PGS_API_URL.'img/android/category-banner.png'?>" />
						</div>
						<div id="category-banners-ios-section" class="device-display hidden">
							<img src="<?php echo PGS_API_URL.'img/ios/category-banners.png'?>" />
						</div>
					</div> 
                </div>   
                
                
                
              </div><!-- #pgs-woo-api-tabs-category-banners -->
              
              
            
                
            <div id="pgs-woo-api-tabs-banner-ads">
            
                <div class="pgs-woo-api-panel" id="pgs-woo-api-banner-ad">
                    <div class="pgs-woo-api-panel-body">
                    <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Banner Ads','pgs-woo-api')?></div>
                    <p class="description"><?php esc_html_e( 'Used for advertisement or special offers, displayed on Home screen.','pgs-woo-api')?></p>
					<p class="note-desc"><strong><?php esc_html_e('IMPORTANT','pgs-woo-api')?>:</strong><br />
						<span class="description">
						<?php 
						echo wp_kses(
							__('For best performance and image quality of Ad banners in the mobile application, strictly use <strong>1080 x 353</strong> (WidthxHeight in px) image for banners.','pgs-woo-api'),
							array(
								'strong' => array()
							)
						);
						?>
						</span>
					</p>
                    
                        <div class="pgs-woo-api-repeater-field-group" data-repeater-list="pgs[banner_ad]">
                        <?php
                        $default_array['banner_ad'] = array(
                            array(
                                'banner_ad_cat_id' => '',
                                'banner_ad_image_id' => ''                                        
                            )           
                        );
                        $banner_ad = array();
                        $banner_ad = $pgs_woo_api_home_option;
                        if(!isset($banner_ad['banner_ad']) || empty($banner_ad['banner_ad'])){
                            $banner_ad = $default_array;
                        }
                         
                        if(isset($banner_ad['banner_ad']) && !empty($banner_ad['banner_ad'])){ 
                            $i = 0;
                            foreach($banner_ad['banner_ad'] as $key => $val){  
                                
                                ?>
                                <div class="pgs-woo-api-field-group" data-repeater-item>                                        
                                    <div class="pgs-woo-api-form-group">
                                        <label><?php esc_html_e( 'Banner Image','pgs-woo-api')?></label>
                                        <?php                                        
                                        if(isset($val['banner_ad_image_id']) && !empty($val['banner_ad_image_id']) ){
                                            $src = wp_get_attachment_image_src($val['banner_ad_image_id'], 'medium' );
                                            if(!empty($src)){
                                                ?>
                                                <div class="upload_image">
                                                    <img src="<?php echo esc_url($src[0])?>" alt="No image" />
                                                </div>
                                                <input type="hidden" name="pgs[banner_ad][<?php echo $i?>][banner_ad_image_id]" id="pgs[banner_ad][<?php echo $i?>]" class="upload_image_id" value="<?php echo $val['banner_ad_image_id']; ?>" />  
                                                <a href="javascript:void(0);" class="upload-image-button button button-default" id="pgs-slider-<?php echo $i?>"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                                <?php
                                            }
                                        } else {?>                                                        
                                            <div class="upload_image"></div>
                                            <input type="hidden" name="pgs[banner_ad][<?php echo $i?>][banner_ad_image_id]" id="pgs[banner_ad][<?php echo $i?>]" class="upload_image_id" value="" />
                                            <a href="javascript:void(0);" class="upload-image-button button button-primary" id="pgs-slider-<?php echo $i?>"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                            <?php
                                        }?>                                        
                                        <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( ! $val['banner_ad_image_id'] ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>                                        
                                    </div>
                                    
                                    <div class="pgs-woo-api-form-group">
                                        <label><?php esc_html_e('Category for Banner Ad','pgs-woo-api')?></label>                                        
                                        <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[banner_ad][<?php echo $i?>][banner_ad_cat_id]">
                                            <option value=""><?php esc_html_e('Select','pgs-woo-api')?></option>
                                            <?php  
                                            $img = '';                                          
                                            $select = pgs_woo_api_get_all_woo_cat();
                                            if(isset($select) && !empty($select)){                                                    
                                                foreach($select as $sekey => $seval){
                                                    $sel = '';
                                                    if($val['banner_ad_cat_id'] == $sekey){
                                                        $sel = 'selected="selected"';    
                                                    }
                                                    echo '<option value="'.$sekey.'" '.$sel.'>'.$seval.'</option>';                                                
                                                }
                                            }?>
                                        </select>                                            
                                    </div>
                                    <span class="removeBanner dashicons dashicons-dismiss" data-repeater-delete><!-- Remove --></span>
                                </div>
                                <?php 
                                $i++;                                                
                            }                                           
                        } ?>                                        
                                
                        </div>
                        <button type="button" class="pgs-woo-api-btn button button-primary" data-repeater-create><?php esc_html_e('Add','pgs-woo-api')?></button>    
                    </div>
                    <div class="pgs-woo-api-panel-sidebar">
						<div class="device-select">
							<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="ad-banner-android-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
							</a>
							<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="ad-banner-ios-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
							</a>
						</div>
						<div id="ad-banner-android-section" class="device-display">
							<img src="<?php echo PGS_API_URL.'img/android/ad-banner.png'?>" />
						</div>
						<div id="ad-banner-ios-section" class="device-display hidden">
							<img src="<?php echo PGS_API_URL.'img/ios/ad-banner.png'?>" />
						</div>
					</div>
                </div>
                
            </div><!-- #pgs-woo-api-tabs-banner-ads -->
            
            <div id="pgs-woo-api-tabs-app-pages">
            
                <div class="pgs-woo-api-panel" id="pgs-woo-api-info-pages">
                    <div class="pgs-woo-api-panel-body">
                    <div class="pgs-woo-api-panel-heading"><?php esc_html_e('App Pages','pgs-woo-api')?></div>  
                    <p class="description"><?php esc_html_e( 'App custom pages.','pgs-woo-api')?></p>
                                        
                        <div class="pgs-woo-api-repeater-field-group" data-repeater-list="pgs[info_pages]"> 
                        <?php
                        /**
                         *  Static pages sections 
                         */ 
                        $static_page = $pgs_woo_api_home_option;                            
                        $about_us = (isset($static_page['static_page']['about_us']))?$static_page['static_page']['about_us']:'';
                        $terms_of_use = (isset($static_page['static_page']['terms_of_use']))?$static_page['static_page']['terms_of_use']:''; 
                        $privacy_policy = (isset($static_page['static_page']['privacy_policy']))?$static_page['static_page']['privacy_policy']:'';
                        ?>
                        <div class="pgs-woo-api-form-group">
                            <label><?php esc_html_e( 'About Us','pgs-woo-api' )?></label>                                        
                            <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[static_page][about_us]">
                                <option value=""><?php esc_html_e('Select page','pgs-woo-api')?></option>
                                <?php                                    
                                $pages = get_pages(); 
                                if(!empty($pages)):
                                    foreach ( $pages as $page ) {                                                    
                                        $sel = '';
                                        if($about_us == $page->ID){
                                            $sel = 'selected="selected"';    
                                        }                                                                                           
                                        $option = '<option value="' . $page->ID . '" '. $sel .'>';                                
                                        $option .= $page->post_title;
                                        $option .= '</option>';
                                        echo $option;
                                    }
                                endif;                                            
                                ?>
                            </select>                        
                                
                        </div>
                        
                        <div class="pgs-woo-api-form-group">
                            <label><?php esc_html_e( 'Terms & Conditions','pgs-woo-api' )?></label>                                        
                            <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[static_page][terms_of_use]">
                                <option value=""><?php esc_html_e('Select page','pgs-woo-api')?></option>
                                <?php                                    
                                $pages = get_pages(); 
                                if(!empty($pages)):
                                    foreach ( $pages as $page ) {                                                    
                                        $sel = '';
                                        if($terms_of_use == $page->ID){
                                            $sel = 'selected="selected"';    
                                        }                                                                                           
                                        $option = '<option value="' . $page->ID . '" '. $sel .'>';                                
                                        $option .= $page->post_title;
                                        $option .= '</option>';
                                        echo $option;
                                    }
                                endif;                                            
                                ?>
                            </select>                                    
                        </div>
                        
                        <div class="pgs-woo-api-form-group">
                            <label><?php esc_html_e( 'Privacy Policy','pgs-woo-api' )?></label>                                        
                            <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[static_page][privacy_policy]">
                                <option value=""><?php esc_html_e('Select page','pgs-woo-api')?></option>
                                <?php                                    
                                $pages = get_pages(); 
                                if(!empty($pages)):
                                    foreach ( $pages as $page ) {                                                    
                                        $sel = '';
                                        if($privacy_policy == $page->ID){
                                            $sel = 'selected="selected"';    
                                        }                                                                                           
                                        $option = '<option value="' . $page->ID . '" '. $sel .'>';                                
                                        $option .= $page->post_title;
                                        $option .= '</option>';
                                        echo $option;
                                    }
                                endif;                                            
                                ?>
                            </select>                        
                                
                        </div>
                        
                        <?php
                        /**
                         *  Dynamic info pages sections 
                         */
                        $default_array['info_pages'] = array(
                            array(
                                'info_pages_page_id' => ''                                                                            
                            )           
                        );
                        $info_pages = array();
                        $info_pages = $pgs_woo_api_home_option;
                        
                        if(!isset($info_pages['info_pages']) || empty($info_pages['info_pages'])){
                            $info_pages = $default_array;
                        }
                         
                        if(isset($info_pages['info_pages']) && !empty($info_pages['info_pages'])){ 
                            $i = 0;
                            foreach($info_pages['info_pages'] as $key => $val){  
                                
                                ?>
                                <div class="pgs-woo-api-form-group" data-repeater-item>
                                    <label><?php esc_html_e( 'Add info Pages','pgs-woo-api' )?></label>
                                    <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs[info_pages][<?php echo $i?>][info_pages_page_id]">
                                        <option value=""><?php esc_html_e('Select page','pgs-woo-api')?></option>
                                        <?php                                    
                                        $pages = get_pages(); 
                                        if(!empty($pages)):
                                            foreach ( $pages as $page ) {                                                    
                                                $sel = '';
                                                if($val['info_pages_page_id'] == $page->ID){
                                                    $sel = 'selected="selected"';    
                                                }                                                                                           
                                                $option = '<option value="' . $page->ID . '" '. $sel .'>';                                
                                                $option .= $page->post_title;
                                                $option .= '</option>';
                                                echo $option;
                                            }
                                        endif;                                            
                                        ?>
                                    </select>                        
                                    <span class="removeBanner dashicons dashicons-dismiss" data-repeater-delete><!-- Remove --></span>    
                                </div>
                                <?php 
                                $i++;                                                
                            }                                           
                        }?>                                        
                        </div>
                        <button type="button" class="pgs-woo-api-btn button button-primary" data-repeater-create><?php esc_html_e('Add','pgs-woo-api')?></button>    
                    </div>
                    <div class="pgs-woo-api-panel-sidebar">
						<div class="device-select">
							<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="pages-android-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
							</a>
							<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="pages-ios-section">
								<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
							</a>
						</div>
						<div id="pages-android-section" class="device-display">
							<img src="<?php echo PGS_API_URL.'img/android/pages.png'?>" />
						</div>
						<div id="pages-ios-section" class="device-display hidden">
							<img src="<?php echo PGS_API_URL.'img/ios/pages.png'?>" />
						</div>
					</div>
                </div>
            </div><!-- #pgs-woo-api-tabs-app-pages -->
                
            
            <div id="pgs-woo-api-tabs-checkout-page">
                
                    <div class="pgs-woo-api-panel" id="pgs-woo-api-checkout-page">
                        <div class="pgs-woo-api-panel-body">
                        <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Checkout Page Setting','pgs-woo-api')?></div>  
                            <p class="description"><?php esc_html_e( 'Checkout page in any e-commerce website plays the most crucial part as it is the online equivalent of a physical checkout counter shown to the customer during the step by step checkout process. Hence, it needs to be appealing enough. Ciya shop comes with a feature that you can personalize this page as well. Although the user is having the default page to use they can enhance their checkout page according to their choice anytime they want by going to \'Checkout Page\' option in \'App settings\' and selecting the page they have created.','pgs-woo-api')?></p>                
                            <div class="pgs-woo-api-form-group">
                                <label><?php esc_html_e( 'Select Checkout Page','pgs-woo-api' )?></label>                                        
                                <select class="pgs-woo-api-form-control pgs-woo-api_pages" name="pgs_checkout_page">
                                    <option value=""><?php echo esc_attr( __( 'Select page' ) ); ?></option>
                                    <?php 
                                    $checkout_page = '';
                                    $checkout_page = get_option('pgs_checkout_page'); 
                                    $pages = get_pages(); 
                                    foreach ( $pages as $page ) {
                                        $selected = '';                                        
                                        if(isset($checkout_page) && !empty($checkout_page)){                                            
                                            if($checkout_page == $page->ID){
                                                $selected = 'selected';   
                                            }    
                                        } else {
                                            $is_page = pgs_woo_api_get_page_title_for_slug('my-account');
                                            if($is_page){
                                                if($is_page->ID == $page->ID){
                                                    $selected = 'selected';   
                                                }                                        
                                            }
                                        }                                        
                                        $option = '<option value="' . $page->ID . '" '. $selected .'>';                                
                                        $option .= $page->post_title;
                                        $option .= '</option>';
                                        echo $option;
                                    }
                                    ?>
                                </select>                        
                            </div>   
                        </div>
                        
                        <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="checkout-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="checkout-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="checkout-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/checkout.png'?>" />
							</div>
							<div id="checkout-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/checkout.png'?>" />
							</div>
						</div>
                    </div> 
              
              </div>
            
            <div id="pgs-woo-api-tabs-social-links">
                
                    <div class="pgs-woo-api-panel" id="">
                        <div class="pgs-woo-api-panel-body">
                            <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Social Media Settings','pgs-woo-api')?></div> 
                            <p class="description"><?php esc_html_e( 'Specify social media URLs, where the user redirected once he/she clicks on social media icons.','pgs-woo-api')?></p> 
                        
                            <?php
                            $facebook = (isset($pgs_woo_api_home_option['pgs_app_social_links']['facebook']))?$pgs_woo_api_home_option['pgs_app_social_links']['facebook']:'';
                            $twitter = (isset($pgs_woo_api_home_option['pgs_app_social_links']['twitter']))?$pgs_woo_api_home_option['pgs_app_social_links']['twitter']:'';
                            $linkedin = (isset($pgs_woo_api_home_option['pgs_app_social_links']['linkedin']))?$pgs_woo_api_home_option['pgs_app_social_links']['linkedin']:'';
                            $google_plus = (isset($pgs_woo_api_home_option['pgs_app_social_links']['google_plus']))?$pgs_woo_api_home_option['pgs_app_social_links']['google_plus']:'';
                            $pinterest = (isset($pgs_woo_api_home_option['pgs_app_social_links']['pinterest']))?$pgs_woo_api_home_option['pgs_app_social_links']['pinterest']:'';
                            $instagram = (isset($pgs_woo_api_home_option['pgs_app_social_links']['instagram']))?$pgs_woo_api_home_option['pgs_app_social_links']['instagram']:'';
                            ?>
                            <div class="pgs-woo-api-field-groups">
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Facebook",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_social_links][facebook]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($facebook)?>" />                        
                                </div>
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Twitter",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_social_links][twitter]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($twitter)?>" />                        
                                </div>
                                
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("LinkedIn",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_social_links][linkedin]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($linkedin)?>" />                        
                                </div>
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Google Plus",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_social_links][google_plus]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($google_plus)?>" />                        
                                </div>                                    
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Pinterest",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_social_links][pinterest]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($pinterest)?>" />                        
                                </div>
                                
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Instagram",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_social_links][instagram]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($instagram)?>" />                        
                                </div>
                            </div>
                        </div>
                        
                       <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="social-links-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="social-links-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="social-links-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/social-links.png'?>" />
							</div>
							<div id="social-links-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/social-links.png'?>" />
							</div>
						</div>               
                    </div>
                
            </div><!-- #pgs-woo-api-tabs-social-links -->
                
            <div id="pgs-woo-api-tabs-feature-box">
                <?php
                $feature_box_heading = (isset($pgs_woo_api_home_option['feature_box_heading']))?$pgs_woo_api_home_option['feature_box_heading']:''; 
                $feature_box_status = (isset($pgs_woo_api_home_option['feature_box_status']) && !empty($pgs_woo_api_home_option['feature_box_status']))?$pgs_woo_api_home_option['feature_box_status']:'enable'; 
                ?>   
                <div class="pgs-woo-api-panel" id="pgs-woo-api-feature-box">
                    <div class="pgs-woo-api-panel-body">
                    <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Feature Box','pgs-woo-api')?></div>  
                    
                        <div class="pgs-woo-api-repeater-field-group" data-repeater-list="pgs[feature_box]">                                
                            <div class="pgs-woo-api-form-groups radio-button-inline">
                                <label><?php esc_html_e("Status",'pgs-woo-api')?></label>                                        
                                <label><input type="radio" name="pgs[feature_box_status]" class="pgs-woo-api-form-control feature-box-status" value="enable" <?php echo ($feature_box_status == "enable")?'checked=""':'';?> /><?php esc_html_e( 'Enable','pgs-woo-api')?></label> 
                                <label><input type="radio" name="pgs[feature_box_status]" class="pgs-woo-api-form-control feature-box-status" value="disable" <?php echo ($feature_box_status == "disable")?'checked=""':'';?> /><?php esc_html_e( 'Disable','pgs-woo-api')?></label>                         
                            </div>
                                                    
                            <div class="pgs-woo-api-field-group feature-box" <?php pgs_woo_api_feature_box_status()?>>
                                <div class="pgs-woo-api-form-group">
                                    <label><?php esc_html_e("Feature Box Title",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[feature_box_heading]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($feature_box_heading)?>" />                        
                                </div>
                            </div>
                            <?php                        
                            $default_array['feature_box'] = array(
                                array(
                                    'feature_image_id' => '',
                                    'feature_title' => '',
                                    'feature_content' => ''
                                )           
                            );
                            $pgs_feature_box = array();
                            $pgs_feature_box = $pgs_woo_api_home_option;
                            if(empty($pgs_feature_box['feature_box'])){
                                $pgs_feature_box = $default_array;
                            } 
                            if(isset($pgs_feature_box) && !empty($pgs_feature_box)){ 
                                foreach($pgs_feature_box['feature_box'] as $key => $val){                                    
                                    ?>
                                    <div class="pgs-woo-api-field-group feature-box" <?php pgs_woo_api_feature_box_status()?> data-repeater-item>
                                        <div class="pgs-woo-api-form-group">
                                            <label><?php esc_html_e("Feature Box Icon Image",'pgs-woo-api')?></label>
											<p><strong><?php esc_html_e('IMPORTANT','pgs-woo-api')?>:</strong><br />
												<span class="description">
												<?php 
												echo wp_kses(
													__('For best performance and image quality, strictly use <strong>150 x 150</strong> (WidthxHeight in px) image for feature box.','pgs-woo-api'),
													array(
														'strong' => array()
													)
												);
												?>
												</span>
											</p>
                                            <?php                                        
                                            if(isset($val['feature_image_id']) && !empty($val['feature_image_id'])){
                                                $src = wp_get_attachment_image_src($val['feature_image_id'], 'thumbnail' );
                                                if(!empty($src)){
                                                    ?>
                                                    <div class="upload_image">
                                                        <img src="<?php echo esc_url($src[0])?>" alt="No image" />
                                                    </div>
                                                    <input type="hidden" name="pgs[feature_box][][feature_image_id]" class="upload_image_id" value="<?php echo $val['feature_image_id']; ?>" />  
                                                    <a href="javascript:void(0);" class="upload-image-button button button-default"><?php esc_html_e( 'Edit','pgs-woo-api')?></a>
                                                    <?php
                                                }
                                            } else {?>                                                        
                                                <div class="upload_image"></div>
                                                <input type="hidden" name="pgs[feature_box][][feature_image_id]" class="upload_image_id" value="" />
                                                <a href="javascript:void(0);" class="upload-image-button button button-primary"><?php esc_html_e( 'Add Image','pgs-woo-api')?></a>
                                                <?php
                                            }?>                                        
                                            <a href="javascript:void(0);" class="remove-image-button button button-default" style="<?php echo ( ! $val['feature_image_id'] ? 'display:none;' : '' ); ?>"><?php esc_html_e( 'Remove','pgs-woo-api')?></a>
                                        </div>                                            
                                            
                                        
                                        <div class="pgs-woo-api-form-group">
                                            <label><?php esc_html_e('Title','pgs-woo-api')?></label>                                        
                                            <input type="text" name="pgs[feature_box][][feature_title]" class="pgs-woo-api-form-control" value="<?php echo $val['feature_title']; ?>"  />                        
                                        </div>
                                        
                                        
                                        <div class="pgs-woo-api-form-group">
                                            <label><?php esc_html_e('Content','pgs-woo-api')?></label>                                        
                                            <textarea class="pgs-woo-api-form-control" name="pgs[feature_box][][feature_content]"><?php echo $val['feature_content']; ?></textarea>                                                               
                                        </div>                                        
                                        <span class="removeBanner dashicons dashicons-dismiss" data-repeater-delete><!-- Remove --></span>
                                    </div>
                                    <?php                                                                                
                                }?>
                                
                                <?php                                           
                            } ?>                    
                            </div>            
                            <button type="button" class="pgs-woo-api-btn button button-primary feature-box" <?php pgs_woo_api_feature_box_status()?> data-repeater-create><?php esc_html_e('Add','pgs-woo-api')?></button>
                        </div>
                    
						<div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="featurebox-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="featurebox-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="featurebox-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/featurebox.png'?>" />
							</div>
							<div id="featurebox-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/featurebox.png'?>" />
							</div>
						</div>
                </div>
            </div><!-- #pgs-woo-api-tabs-feature-box -->
            
            
            <div id="pgs-woo-api-tabs-contact-info">
                
                    <div class="pgs-woo-api-panel" id="">
                        <div class="pgs-woo-api-panel-body">
                            <div class="pgs-woo-api-panel-heading"><?php esc_html_e('Contact Info','pgs-woo-api')?></div>                        
                            <?php
                            $address_line_1 = (isset($pgs_woo_api_home_option['pgs_app_contact_info']['address_line_1']))?$pgs_woo_api_home_option['pgs_app_contact_info']['address_line_1']:'';
                            $address_line_2 = (isset($pgs_woo_api_home_option['pgs_app_contact_info']['address_line_2']))?$pgs_woo_api_home_option['pgs_app_contact_info']['address_line_2']:'';
                            $email = (isset($pgs_woo_api_home_option['pgs_app_contact_info']['email']))?$pgs_woo_api_home_option['pgs_app_contact_info']['email']:'';
                            $phone = (isset($pgs_woo_api_home_option['pgs_app_contact_info']['phone']))?$pgs_woo_api_home_option['pgs_app_contact_info']['phone']:'';                            
                            ?>
                            <div class="pgs-woo-api-field-groups">
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Address Line 1",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_contact_info][address_line_1]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($address_line_1)?>" />                        
                                </div>
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Address Line 2",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_contact_info][address_line_2]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($address_line_2)?>" />                        
                                </div>
                                
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Email",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_contact_info][email]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($email)?>" />                        
                                </div>
                                <div class="pgs-woo-api-form-groups">
                                    <label><?php esc_html_e("Phone",'pgs-woo-api')?></label>                                        
                                    <input type="text" name="pgs[pgs_app_contact_info][phone]" class="pgs-woo-api-form-control" value="<?php echo esc_attr($phone)?>" />                        
                                </div>                                
                            </div>
                        </div>
                        
                       <div class="pgs-woo-api-panel-sidebar">
							<div class="device-select">
								<a href="javascript:void(0)" class="button button-primary android-device pgs-woo-device-img-display active" data-target="contact-info-android-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/android/android.png'?>" />
								</a>
								<a href="javascript:void(0)" class="button button-primary ios-device pgs-woo-device-img-display" data-target="contact-info-ios-section">
									<img class="img-responsive" src="<?php echo PGS_API_URL.'img/ios/ios.png'?>" />
								</a>
							</div>
							<div id="contact-info-android-section" class="device-display">
								<img src="<?php echo PGS_API_URL.'img/android/contact-info.png'?>" />
							</div>
							<div id="contact-info-ios-section" class="device-display hidden">
								<img src="<?php echo PGS_API_URL.'img/ios/contact-info.png'?>" />
							</div>
						</div>               
                    </div>
                
            </div><!-- #pgs-woo-api-tabs-contact-info -->
            
        </div>
    </form>
</div>    
    <?php
}?>