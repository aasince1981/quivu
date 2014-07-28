//
//  WelcomeScreenViewController.h
//  Reloved
//
//  Created by Kamal on 09/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <FacebookSDK/FacebookSDK.h>

@interface WelcomeScreenViewController : UIViewController<CLLocationManagerDelegate,UIWebViewDelegate>
{
    NSString * DeviceType;
    NSString * Type;
    NSString * gender;
    NSUserDefaults * defaults;
    NSString * UserSocialId;
    CLLocationManager *locationManager;
    IBOutlet UILabel * lblWelcome;
    IBOutlet UILabel * lblRecommend;
    IBOutlet UILabel * lblAlreadySignUP;
    IBOutlet UILabel * lblPost;
    IBOutlet UILabel * lblOr;
    IBOutlet UIButton * BtnLoginNow;
    
    NSMutableData *receivedData;
    NSString * AccessToken;
    IBOutlet UIView * View_Webview;
     NSArray *items;
    
    IBOutlet UIImageView * imgLogo;
}

@property (nonatomic, strong) IBOutlet UIWebView *webview;
@property (nonatomic, retain) NSString *isLogin;
@property (assign, nonatomic) Boolean isReader;

- (void) facebook:(id) dic;

- (IBAction)BtnSignUpWithEmail:(id)sender;
- (IBAction)BtnConnectWithFacebook:(id)sender;
- (IBAction)BtnLoginNow:(id)sender;
- (IBAction)btnSignInWithGoogle:(id)sender;
- (IBAction)BtnCross:(id)sender;

@end


