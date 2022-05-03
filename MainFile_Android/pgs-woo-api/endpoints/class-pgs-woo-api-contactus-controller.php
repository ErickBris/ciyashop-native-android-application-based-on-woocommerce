<?php
if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

class PGS_WOO_API_ContactusController extends PGS_WOO_API_Controller{
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
	protected $rest_base = 'contactus';
    	
	public function __construct() {
		$this->register_routes();
	}
	public function register_routes() {		
		add_action( 'rest_api_init', array( $this, 'pgs_woo_api_register_route'));
	}
	
	
	public function pgs_woo_api_register_route() {        
        
        
        register_rest_route( $this->namespace, $this->rest_base, array(
    		'methods' => WP_REST_Server::CREATABLE,//'POST',
    		'callback' => array( $this, 'pgs_woo_api_contactus'),
            'permission_callback' => array($this, 'pgs_woo_api_permission_callback'),
    	) );
        
    }
    
    
    /**
    * URL : http://yourdomain.com/wp-json/pgs-woo-api/v1/contactus
    * @param name: ####
    * @param subject: ####
    * @param email: ####
    * @param message: ####    
    */
    public function pgs_woo_api_contactus(){        
        
        $input = file_get_contents("php://input");
        $request = json_decode($input,true);
        
        $required = array( 'name','email','message','subject' );
        $validation = $this->pgs_woo_api_param_validation( $required, $request );
        if($validation){
           return $validation; 
        }
	       
		
        
        $error = array( "status" => "error" );        
        
        if ( isset($request['name']) && empty($request['name'])) {
        	
            $error['message'] = esc_html__("Please enter your name",'pgs-woo-api');
            return $error;             
            
        }
        
        if ( isset($request['subject']) && empty($request['subject'])) {
        	
            $error['message'] = esc_html__("Please enter subject",'pgs-woo-api');
            return $error;             
            
        }
        
        if (empty($request['email'])) {
        	$error['message'] = esc_html__("Please enter your email",'pgs-woo-api');
            return $error;
            
        }
        
        if ( !is_email(  $request['email']) ) {         
            $error['message'] = esc_html__("Please enter valid email address",'pgs-woo-api');
            return $error;                            
        }
        
        if (empty($request['message'])) {
        	$error['message'] = esc_html__("Message can't be blank",'pgs-woo-api');
            return $error;
            
        }
        $contact_no = '';
        if ( isset($request['contact_no']) && !empty($request['contact_no'])) {            
            $contact_no = trim(sanitize_text_field($request['contact_no']));
        }
        
        $name = trim(sanitize_text_field($request['name']));
        $site_name = get_bloginfo( 'name' );
        $admin_email = get_bloginfo( 'admin_email' ); 
        $woocommerce_email_from_name = get_option('woocommerce_email_from_name');
        $from_name = (isset($woocommerce_email_from_name))?$woocommerce_email_from_name:$site_name;
        
        $woocommerce_email_from_address = get_option('woocommerce_email_from_address'); 
        $from_email = (isset($woocommerce_email_from_address))?$woocommerce_email_from_address:$admin_email;
        
        $to = trim(sanitize_text_field($from_email));
        $reply_to = trim(sanitize_text_field($request['email']));        
        $subject = trim(sanitize_text_field($request['subject']));
        
        $cfmsg = sprintf( esc_html__( 'Message from %s contact us form', 'pgs-woo-api' ), $site_name );
        $user_details = esc_html__( 'User Details : ', 'pgs-woo-api' );
        $name = sprintf( esc_html__( 'Name : %s', 'pgs-woo-api' ), $name );
        $reply_to = sprintf( esc_html__( 'Email : %s', 'pgs-woo-api' ), $reply_to );
        $subject = sprintf( esc_html__( 'Subject : %s', 'pgs-woo-api' ), $subject );
        $contact_no = sprintf( esc_html__( 'Contact No : %s', 'pgs-woo-api' ), $contact_no );
        $message_lbl = esc_html__( 'Message : ', 'pgs-woo-api' );                
                
        $message  = "<p>".$cfmsg. "</p>"; 
        $message .= "<p>".$user_details . "</p>";
        $message .= "<p>".$name . "</p>";
        $message .= "<p>".$reply_to . "</p>";
        $message .= "<p>".$subject . "</p>";
        $message .= "<p>".$contact_no . "</p>";        
        $message .= "<p>".$message_lbl . "</p>";                     
        $message .= "<p>".$request['message']."</p>"; 
              
        $headers[] = 'Content-Type: text/html; charset=UTF-8';
        //$headers[] = 'From: '.$from_name.' <'.$from_email.'>' . "\r\n";        
        $headers[] = 'Reply-To: ' . $reply_to . "\r\n" ;        
        if ( !wp_mail($to, $subject, $message,$headers) ){            
            $error['error'] = esc_html__("The e-mail could not be sent.Please try after some time.");
            return $error;                        
        }       
        
		return array(
			"status"  => "success",
            "message" => esc_html__("Your email was successfully sent. We will contact you as soon as possible","pgs-woo-api"),
		);                            
    }   
 }
 new PGS_WOO_API_ContactusController;