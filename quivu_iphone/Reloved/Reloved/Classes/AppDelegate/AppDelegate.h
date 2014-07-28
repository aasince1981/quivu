//
//  AppDelegate.h
//  Reloved
//
//  Created by Kamal on 09/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "WelcomeScreenViewController.h"
#import "LeveyTabBarController.h"
#import <AudioToolbox/AudioToolbox.h>
#import <FacebookSDK/FacebookSDK.h>
#import "SplashScreen.h"
#import "LeveyTabBar.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate,CLLocationManagerDelegate,UINavigationControllerDelegate,UITabBarControllerDelegate,LeveyTabBarControllerDelegate,UIAlertViewDelegate>
{
    NSMutableDictionary * dictServer;
    LeveyTabBarController * leveyTabBarController;
    NSArray * permissions;
}

@property (nonatomic, retain) IBOutlet LeveyTabBarController *leveyTabBarController;
@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UINavigationController * navigationController;
@property (strong, nonatomic) WelcomeScreenViewController * vcWelcome;
@property (strong, nonatomic) SplashScreen * vcSplash;

@property (strong, nonatomic) UIButton * btnTabActivity;
@property (nonatomic, retain) IBOutlet LeveyTabBar *leveyTabBar;

@property (strong, nonatomic) NSMutableDictionary * baseurl;
@property (strong, nonatomic) NSString * apnsToken;
@property (strong, nonatomic) NSMutableDictionary * dictLoginInfo;
@property (strong, nonatomic) NSString * timeZone;
@property (strong, nonatomic) NSMutableDictionary * DictEditProductInfo;
@property (strong, nonatomic) NSString * Latitude;
@property (strong, nonatomic) NSString * Longitude;
@property (strong, nonatomic) NSString * CityName;
@property  int CategoryId;

@property (strong, nonatomic) NSMutableDictionary * dictDeleteProductId;
@property (strong, nonatomic) NSMutableDictionary * dictCategoryInfo;

@property id MyVC;

@property (strong, nonatomic) NSString * NearByPlace;
@property (strong, nonatomic) UIImage * imgCoverPhoto;
@property BOOL boolImageSelect;
@property BOOL boolEditItem;
@property BOOL boolForEditProduct;
@property BOOL boolPopController;

@property BOOL boolImage1;
@property BOOL boolImage2;
@property BOOL boolImage3;
@property BOOL boolImage4;

@property (strong, nonatomic) NSMutableDictionary * dictImages;

- (void) setDictServer:(NSString *) base;
- (NSString *) getDictServer:(NSString *) key;
+ (CLLocationManager *) getLoactionManager;

- (void) TabBarShow;
- (NSString *) getTimeDiffrece:(NSString *) messageTime;
-(BOOL)isInternetAvailable;

extern NSString *const FBSessionStateChangedNotification;
- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI;

@end





