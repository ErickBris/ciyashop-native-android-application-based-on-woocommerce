jQuery(document).ready(function() {

    /**
    * On update option or settings show loader
    */
    if(document.getElementById('publish-btn')){
        jQuery('#publish-btn').on('click',function(){
            jQuery('.spinner').css('visibility','visible');
        });
    }
    /** End **/

    /**
    * Show / Hide options
    */
    jQuery('.feature-box-status').on('click',function(){
        var status = jQuery(this).val();
        if(status == "disable"){
            jQuery('.feature-box').hide();
        } else {
            jQuery('.feature-box').show();
        }
    });
    /** End **/


    /**
    * Main Category Icon image.
    */
    var get_cat_id;var main_category_icon;var get_cat_icon;var cat_media_holder;
    var removeimage;var cat_image_id;var imagelbl;var get_cat_icon_id;
    jQuery(document).on('change','.pgs-woo-api_main_category',function(){
        get_cat_id = jQuery(this).val();
        get_cat_icon = jQuery(this).find(':selected').attr('data-caticon');
        get_cat_icon_id = jQuery(this).find(':selected').attr('data-caticonid');
        main_category_icon = jQuery(this).parents('.pgs-woo-api-field-group').find('.pgs-woo-api_main_category_icon');
        cat_media_holder = jQuery(main_category_icon).find('.upload_image');
        removeimage = jQuery(main_category_icon).find('.remove-image-button');
        cat_image_id = jQuery(main_category_icon).find('.upload_image_id');
        imagelbl = jQuery(main_category_icon).find('.upload-image-button');
        if(get_cat_id){
            if(get_cat_icon != ''){
                var img = '<img src="' + get_cat_icon + '" width="150px" height="150px" />';
                jQuery(removeimage).show();
                jQuery(cat_image_id).val(get_cat_icon_id);
                jQuery(imagelbl).text("Edit");
            } else {
                var img = '';
                jQuery(removeimage).hide();
                jQuery(cat_image_id).val('');
                jQuery(imagelbl).text("Add Image");
            }
            jQuery(cat_media_holder).html(img);
            jQuery(main_category_icon).show();
        } else {
            jQuery(main_category_icon).hide();
        }
    });
    /** End **/

    /**
    * Add Datepiker on user date of birth field
    */
    if(document.getElementById('dob')){
        jQuery( "#dob" ).datepicker({
          dateFormat: "mm/dd/yy"
        });
    }
    /** End **/

    /*
    * Open filte upload onclick button
    */
    if(document.getElementById('open_pem_file_dev')){
        jQuery('#open_pem_file_dev').on('click',function(event){
            event.preventDefault();
            jQuery('#pem_file_dev').trigger('click');
        });

        jQuery(document).on('change','#pem_file_dev',function() {
            var dev_file = this.files && this.files.length ? this.files[0].name : '';
            jQuery('#pem-dev-file-desc').text( dev_file );
        });

    }
    if(document.getElementById('open_pem_file_pro')){

        jQuery('#open_pem_file_pro').on('click',function(event){
            event.preventDefault();
            jQuery('#pem_file_pro').trigger('click');
        });

        jQuery(document).on('change','#pem_file_pro',function() {
            var pro_file = this.files && this.files.length ? this.files[0].name : '';
            jQuery('#pem-pro-file-desc').text(pro_file);
        });
    }
    /** End **/


    /**
    *
    * WP Media Upload popup box
    *
    */
    var mediaUploader;var $this;
    jQuery(document).on( 'click','.upload-image-button', function ( event ) {
        event.preventDefault();
        $this = jQuery( this ),
			current_parent = jQuery(this).closest('.pgs-woo-api-field-group');
			media_holder = jQuery(current_parent).find('.upload_image');
            hd_image_id = jQuery(current_parent).find('.upload_image_id');

        // If the uploader object has already been created, reopen the dialog
        if (mediaUploader) {
            mediaUploader.open();
            return;
        }
        // Extend the wp.media object
        mediaUploader = wp.media.frames.file_frame = wp.media({
            title: pgs_woo_api.choose_image,
            button: {
            text: pgs_woo_api.choose_image,
        }, multiple: false });

        // When a file is selected, grab the URL and set it as the text field's value
        mediaUploader.on('select', function() {
            var attachment = mediaUploader.state().get('selection').first().toJSON();
            jQuery(current_parent).find('.upload-image-button').removeClass('button button-primary').addClass('button button-default').html('Edit');
            var img = '<img src="' + attachment.url + '" width="150px" height="150px" />';
            jQuery(media_holder).html(img);
            jQuery(hd_image_id).val( attachment.id );
            jQuery(current_parent).find('.remove-image-button').show();
        });
        // Open the uploader dialog
        mediaUploader.open();
    });

    //Remove image on click remove button
	jQuery(document).on( 'click', '.remove-image-button', function( event ) {
		event.preventDefault();
		var $this = jQuery( this );
		$this.parent().find('.upload_image').html( '' );
        $this.parent().find('.upload-image-button').removeClass('button button-default').html('Edit');
        $this.parent().find('.upload-image-button').addClass('button button-primary').html(pgs_woo_api.add_image);

		$this.parent().find('.remove-image-button').hide();
		$this.parent().find('.upload_image_id').val( 0 );
	} );
    /** End **/

    // Add Color Picker to all inputs that have 'color-field' class
    jQuery('.cpa-color-picker').wpColorPicker();


    /*
    * Vertical tabs
    */
    if(document.getElementById('pgs-woo-api-tabs')){
        var index = 'pgs-woo-api-active-tab';
        //  Define friendly data store name
        var store_data = window.sessionStorage;
        var old_index = 0;
        //  Start magic!
        try {
            // getter: Fetch previous value
            old_index = store_data.getItem(index);
        } catch(e) {}
        var pgsTabs = jQuery( "#pgs-woo-api-tabs" ).tabs({
            active: old_index,
            activate: function(event, ui) {
                //  Get future value
                var new_index = ui.newTab.parent().children().index(ui.newTab);
                //  Set future value
                try {
                    store_data.setItem( index, new_index );
                    old_id = ui.newPanel.attr('id');
                } catch(e) {}
            }
        }).addClass( "ui-tabs-vertical ui-helper-clearfix" );
        jQuery( "#pgs-woo-api-tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
    }
    /** End **/


    /*
    * Repeater Field
    */
    'use strict';
    jQuery('#pgs-woo-api-main_slider').repeater({
        show: function () {
            jQuery('.upload_image', this).html('');
            jQuery('.upload-image-button', this).removeClass('button button-default').html('Edit');
            jQuery('.upload-image-button', this).addClass('button button-primary').html(pgs_woo_api.add_image);
            jQuery('.remove-image-button', this).hide();
            jQuery(this).slideDown();
        },
        hide: function (deleteElement) {
            //if(confirm(pgs_woo_api.delete_msg)) {
                jQuery(this).slideUp(deleteElement);
            //}
        },
        ready: function (setIndexes) {
        }
    });

    jQuery('#pgs-woo-api-category-banners,#pgs-woo-api-banner-ad,#pgs-woo-api-feature-box').repeater({
        show: function () {
            jQuery('.upload_image', this).html('');
            jQuery('.upload-image-button', this).removeClass('button button-default').html('Edit');
            jQuery('.upload-image-button', this).addClass('button button-primary').html(pgs_woo_api.add_image);
            jQuery('.remove-image-button', this).hide();
            jQuery(this).slideDown();
        },
        hide: function (deleteElement) {
            //if(confirm(pgs_woo_api.delete_msg)) {
                jQuery(this).slideUp(deleteElement);
            //}
        },
        ready: function (setIndexes) {
        }
    });

    jQuery('#pgs-woo-api-info-pages,#pgs-woo-api-main-category').repeater({
        initEmpty: false,
        show: function () {
            jQuery('.upload_image', this).html('');
            jQuery('.upload-image-button', this).removeClass('button button-default').html('Edit');
            jQuery('.upload-image-button', this).addClass('button button-primary').html(pgs_woo_api.add_image);
            jQuery('.remove-image-button', this).hide();
            jQuery(this).slideDown();

            if( jQuery(this).parents("#pgs-woo-api-main-category").attr("data-limit").length > 0 ){
                if( jQuery(this).parents("#pgs-woo-api-main-category").find("div[data-repeater-item]").length <= $(this).parents("#pgs-woo-api-main-category").attr("data-limit") ){
                    jQuery(this).slideDown();
                } else {
                    jQuery(this).remove();
                }
            } else {
                jQuery(this).slideDown();
            }

        },
        hide: function (deleteElement) {
            //if(confirm(pgs_woo_api.delete_msg)) {
                jQuery(this).slideUp(deleteElement);
            //}
        },
        ready: function (setIndexes) {
        }
    });

    /*
    * Remove app cat image on add new image with ajax
    */
    jQuery('input[name=submit]').on( 'click', function() {
        setTimeout(function(){
            jQuery( '#product_app_cat_thumbnail' ).find( 'img' ).attr( 'src', pgs_woo_api.pgs_api_url+'img/pgs_app_cat_placeholder.jpg' );
            jQuery('#product_app_cat_thumbnail_id').val( 0 );
            jQuery( '.remove_app_image_button' ).hide();
        },2000);
    } );
    /** End **/


    /**
    * Second media Popup box for App cat image
    */
    var mediaUploader;
    jQuery(document).on( 'click','#upload-image-button', function ( event ) {
        event.preventDefault();
        var $this = jQuery( this ),
			current_parent = jQuery(this).closest('.form-field');
			media_holder = jQuery(current_parent).find('.upload_image');

        // If the uploader object has already been created, reopen the dialog
        if (mediaUploader) {
            mediaUploader.open();
            return;
        }
        // Extend the wp.media object
        mediaUploader = wp.media.frames.file_frame = wp.media({
            title: pgs_woo_api.choose_image,
            button: {
            text: pgs_woo_api.choose_image
        }, multiple: false });

        // When a file is selected, grab the URL and set it as the text field's value
        mediaUploader.on('select', function() {
            var attachment = mediaUploader.state().get('selection').first().toJSON();
            var img = '<img src="' + attachment.url + '" width="60px" height="60px" />';
            jQuery(media_holder).html(img);
            jQuery(current_parent).find('.upload_image_id').val(attachment.id);
            jQuery(current_parent).find('.remove_app_image_button').show();
        });
        // Open the uploader dialog
        mediaUploader.open();
    });

    jQuery(document).on( 'click', '#remove-image-button', function( event ) {
		event.preventDefault();
		var $this = jQuery( this );
		current_parent = jQuery(this).closest('.form-field');
		jQuery( '#product_app_cat_thumbnail' ).find( 'img' ).attr( 'src', pgs_woo_api.pgs_api_url+'img/pgs_app_cat_placeholder.jpg' );
		jQuery(current_parent).find('.upload_image_id').val( 0 );
        jQuery( '.remove_app_image_button' ).hide();
	} );
    /** End **/

    /**
    * Call test api
    */
    if(document.getElementById('test-api-btn')){
        $(document).on('click','#test-api-btn',function(){
            $.ajax({
        		url: ajaxurl,
        		type: 'post',
                dataType: 'json',
        		data:{action:'pgs_woo_api_test_api_ajax_call'},
                beforeSend: function(){
                    $('.pgs-loader').addClass('pgs-api-console');
                    $('.pgs-loader').html('loading..');
                },
        		success: function(response){
                    $('.pgs-loader').html(response);
        		},
        		error: function(msg){
        			alert('Something went wrong!');
                    $('.pgs-loader').removeClass('pgs-api-console');
                    $('.pgs-loader').html('');

        		}
        	});
        });
    }

    /**
    * Submit token generations proccess form fields to ajax
    */
    $(document).on('click','.token-gen-pro',function(){
        var id = $(this).attr('id');
        var form_data = '';
        if(id == "stp-1"){
            //var form_data = $(".step-1 :input");
            form_data += '<input type="hidden" name="step" value="1" />';
        } else if(id == "stp-2"){
            var oauth_consumer_key = $("input[name=oauth_consumer_key]").val();
            var oauth_consumer_secret = $("input[name=oauth_consumer_secret]").val();
            var oauth_token = $("input[name=oauth_token]").val();
            var oauth_token_secret = $("input[name=oauth_token_secret]").val();
            var oauth_verifier = $("input[name=oauth_verifier]").val();

            form_data += '<input type="hidden" name="oauth_consumer_key" value="'+oauth_consumer_key+'" />';
            form_data += '<input type="hidden" name="oauth_consumer_secret" value="'+oauth_consumer_secret+'" />';
            form_data += '<input type="hidden" name="oauth_token" value="'+oauth_token+'" />';
            form_data += '<input type="hidden" name="oauth_token_secret" value="'+oauth_token_secret+'" />';
            form_data += '<input type="hidden" name="oauth_verifier" value="'+oauth_verifier+'" />';
            form_data += '<input type="hidden" name="step" value="2" />';
        }

        jQuery('<form>', {
            "id": "getPgsApiData",
            "html": form_data,
            "action": ''
        }).appendTo(document.body).submit();
    });

    /**
    * On update option or settings show loader
    */
    if(document.getElementById('geo-publish-btn')){
        jQuery('#geo-publish-btn').on('click',function(){
            jQuery('.spinner').css('visibility','visible');
        });
    }

	/**
    * Display device images based on device selected
    */
    if(document.getElementsByClassName('pgs-woo-device-img-display')){
        jQuery('.pgs-woo-device-img-display').on('click',function(e){
			e.preventDefault();
			jQuery(this).parents(".pgs-woo-api-panel-sidebar").find('.pgs-woo-device-img-display').removeClass("active");
			jQuery(this).addClass("active");
            jQuery(this).parents(".pgs-woo-api-panel-sidebar").find('.device-display').addClass('hidden');
			jQuery("#"+ jQuery(this).data('target')).removeClass('hidden');
        });
    }
    jQuery('.credentials-code-device-img').on('click', function (e) {
        e.preventDefault();

        jQuery('.credentials-code-device-img').removeClass('active');
        jQuery(this).addClass('active');

        var credentials_code = 'Something went wrong!',
            client_key = jQuery('input.client_key').val(),
            client_secret = jQuery('input.client_secret').val(),
            token = jQuery('input.token').val(),
            token_secret = jQuery('input.token_secret').val(),
            consumer_key = jQuery('input.consumer_key').val(),
            consumer_secret = jQuery('input.consumer_secret').val(),
            site_url = jQuery('input.pgs-site-url').val();

        if(client_key == '' || client_key == '' || token == '' || token_secret == '' || consumer_key == '' || consumer_secret == '') {
            credentials_code = 'Something went wrong!';
            jQuery('#credentials-code-api-btn').hide();
        } else if(jQuery(this).data('target') == 'credentials-code-android') {
            credentials_code = 'public final String APP_URL = "'+ site_url +'/";\npublic final String WOO_MAIN_URL = APP_URL + "wp-json/wc/v2/";\npublic final String MAIN_URL = APP_URL + "wp-json/pgs-woo-api/v1/";\n\npublic static final String CONSUMERKEY = "'+ client_key +'";\npublic static final String CONSUMERSECRET = "'+ client_secret +'";\npublic static final String OAUTH_TOKEN = "'+ token +'";\npublic static final String OAUTH_TOKEN_SECRET = "'+ token_secret +'";\n\npublic static final String WOOCONSUMERKEY = "'+ consumer_key +'";\npublic static final String WOOCONSUMERSECRET = "'+ consumer_secret +'";';
            jQuery('#credentials-code-api-btn').show();
        } else if(jQuery(this).data('target') == 'credentials-code-ios') {
            credentials_code = '#define OAUTH_CUSTOMER_KEY @"'+ consumer_key +'" \n#define OAUTH_CUSTOMER_SERCET @"'+ consumer_secret +'"\n\n#define OAUTH_CONSUMER_KEY_PLUGIN @"'+ client_key +'"\n#define OAUTH_CONSUMER_SECRET_PLUGIN @"'+ client_secret +'"\n#define OAUTH_TOKEN_PLUGIN @"'+ token +'"\n#define OAUTH_TOKEN_SECRET_PLUGIN @"'+ token_secret +'"\n\n#define appURL @"'+ site_url +'/"\n#define PATH appURL@"wp-json/wc/v2/"\n#define OTHER_API_PATH appURL@"wp-json/pgs-woo-api/v1/"';
            jQuery('#credentials-code-api-btn').show();
        }
        jQuery('.pgs-woo-api-credentials-code').removeClass('pgs-hidden');
        jQuery('.pgs-woo-api-credentials-code').text(credentials_code);

    });

    jQuery('#credentials-code-api-btn').on('click', function(e) {
        e.preventDefault();
        jQuery('.pgs-woo-api-credentials-code').select();
        document.execCommand("copy");
    })
});