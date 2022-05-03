<?php
add_filter( 'wcal_abandoned_orders_columns', 'pgs_woo_api_add_colum_for_cart_from_web_or_app' );
function pgs_woo_api_add_colum_for_cart_from_web_or_app( $columns ){
    $columns['abandoned_from'] = esc_html__( 'Abandoned From', 'pgs-woo-api' );
    return $columns;
}


add_filter( 'wcal_abandoned_orders_table_data', 'pgs_woo_api_add_colum_data_for_cart_from_web_or_app' );
function pgs_woo_api_add_colum_data_for_cart_from_web_or_app( $return_abandoned_orders_display ){
    
    foreach($return_abandoned_orders_display as $key => $data){        
        $id = $return_abandoned_orders_display[$key]->id;
        $device_type = get_abandon_cart_data_by_id($id);
        $return_abandoned_orders_display[$key]->abandoned_from = $device_type;        
    }    
    return $return_abandoned_orders_display;
}

function get_abandon_cart_data_by_id($id){
    global $wpdb;
    $query   = "SELECT * FROM ".$wpdb->prefix."pgs_woo_api_abandoned_cart_meta WHERE abandoned_cart_id = %d";
    $results = $wpdb->get_row( $wpdb->prepare( $query, $id ));    
    if(isset($results->device_type) && !empty($results->device_type)){
        return $results->device_type;
    } else {
        return 'Web';
    }
}

function pgs_woo_api_send_abandon_cart_notification_to_customer($email){
    
    $user = get_user_by( 'email',$email );        
    $data = pgs_woo_api_get_push_notification_data($user->ID);
    if(!empty($data)){        
        $devicedata = array();
        foreach($data as $val){
            $devicedata[] = $val->device_token;                                             
        }
    }
    if(!empty($devicedata)){
        $device_tokens = implode('\',\'',$devicedata);    
    }    
    global $wpdb;
    $query   = "SELECT * FROM ".$wpdb->prefix."pgs_woo_api_abandoned_cart_meta WHERE device_token IN ('$device_tokens')";
    $results = $wpdb->get_results( $query,OBJECT);
    $device_data = array();
    
    if(!empty($results)):
            foreach($results as $result){
    
                if(isset($result->device_token) && !empty($result->device_token)){
                    $type = ($result->device_type == "iOS")?1:2;
                    $device_data[] = array(
                        'token' => $val->device_token,
                        'type' => $type
                    );        
                    $notification_code = 4; 
                    $msg = get_bloginfo('name');
                    $custom_msg = esc_html__( 'Abandon cart alert!','pgs-woo-api');
                    $badge = 0;
                    $push = new PGS_WOO_API_Controller;
                    $push->send_push( $msg, $badge, $custom_msg,$notification_code,$device_data);
                }
            }
    endif;
}