//
//  AppDelegate.m
//  Reloved
//
//  Created by Kamal on 09/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "AppDelegate.h"
#import "LeveyTabBarController.h"
#import "BrowseScreen.h"
#import "ActivityViewController.h"
#import "FollowingViewController.h"
#import "MeViewController.h"
#import "SettingViewController.h"
#import "CameraTest.h"

@implementation AppDelegate
NSString *const FBSessionStateChangedNotification = @"com.it.quivu:FBSessionStateChangedNotification";

@synthesize MyVC, dictImages, vcSplash,navigationController,baseurl,apnsToken,dictLoginInfo,leveyTabBarController,timeZone, NearByPlace,boolImageSelect,boolForEditProduct,vcWelcome,DictEditProductInfo, boolPopController,boolEditItem,Latitude,Longitude,CityName,dictCategoryInfo, btnTabActivity, leveyTabBar,CategoryId,boolImage1,boolImage2,boolImage3,boolImage4,dictDeleteProductId;

static CLLocationManager *locationManager;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:(UIRemoteNotificationType)(UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound)];
    
    vcSplash = [[SplashScreen alloc] initWithNibName:@"SplashScreen" bundle:nil];
    vcWelcome = [[WelcomeScreenViewController alloc] initWithNibName:@"WelcomeScreenViewController" bundle:nil];
    
    navigationController=[[UINavigationController alloc]initWithRootViewController:vcSplash];
    [self BaseUrl];
    
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    [locationManager startUpdatingLocation];
    
    dictCategoryInfo = [[NSMutableDictionary alloc]init];
    dictDeleteProductId = [NSMutableDictionary new];
    
    if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    }

    [self.navigationController.navigationBar setHidden:YES];
    [self.window addSubview:navigationController.view];
    self.window.rootViewController = navigationController;
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];

    return YES;
}

- (void) BaseUrl {

    //if([obNet InternetStatus:YES]) {
        
        if([KAppDelegate isInternetAvailable]) {
            NSString *Webservice_url = [NSString stringWithFormat:@"http://mobilitytesting.com/quivu-app/webServices/baseUrl.php?method=baseURL"];
            [obNet JSONFromWebServices:Webservice_url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
                NSLog(@"json-%@", json);
                if ([obNet isObject:json String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
                    NSMutableDictionary * dictweb_service_links = [json objectForKey:@"web_service_links"];
                    KAppDelegate.baseurl = dictweb_service_links;
                    NSString * baseURL = [dictweb_service_links objectForKey:@"webServicesDirectory"];
                    [KAppDelegate setDictServer:baseURL];
                    
                    KAppDelegate.timeZone = [KAppDelegate.baseurl objectForKey:@"timeZone"];
                }
            }];
        }
       
   // }
}

// webservices methods.......
- (void) setDictServer:(NSString *) base
{
    dictServer = [[NSMutableDictionary alloc] init];
    [dictServer setObject:[NSString stringWithFormat:@"%@userRegistration.php?method=userRegistrations", base] forKey:WS_UserRegistrations];
    [dictServer setObject:[NSString stringWithFormat:@"%@login.php?method=checkLogin", base] forKey:WS_Login];
    [dictServer setObject:[NSString stringWithFormat:@"%@forgetPassword.php?method=forgetPassword", base] forKey:WS_ForgotPassword];
    [dictServer setObject:[NSString stringWithFormat:@"%@getCategory.php?method=getCategories", base] forKey:WS_GetCategories];
    [dictServer setObject:[NSString stringWithFormat:@"%@profileView.php?method=viewProfile", base] forKey:WS_ViewProfile];
    [dictServer setObject:[NSString stringWithFormat:@"%@changePassword.php?method=ChangePasssword", base] forKey:WS_ChangePasssword];
    [dictServer setObject:[NSString stringWithFormat:@"%@getProduct.php?method=getProducts", base] forKey:WS_GetProducts];
    [dictServer setObject:[NSString stringWithFormat:@"%@likes.php?method=addLike", base] forKey:WS_AddLike];
    [dictServer setObject:[NSString stringWithFormat:@"%@products.php?method=viewProduct", base] forKey:WS_ViewProduct];
    [dictServer setObject:[NSString stringWithFormat:@"%@offer.php?method=addOffer", base] forKey:WS_AddOffer];
    [dictServer setObject:[NSString stringWithFormat:@"%@follow.php?method=getFollowing", base] forKey:WS_GetFollowing];
    [dictServer setObject:[NSString stringWithFormat:@"%@follow.php?method=getFollower", base] forKey:WS_GetFollower];
    [dictServer setObject:[NSString stringWithFormat:@"%@follow.php?method=addFollow", base] forKey:WS_AddFollow];
    [dictServer setObject:[NSString stringWithFormat:@"%@editProfile.php?method=editProfile", base] forKey:WS_EditProfile];
    [dictServer setObject:[NSString stringWithFormat:@"%@searchProduct.php?method=productFilter", base] forKey:WS_ProductFilter];
    [dictServer setObject:[NSString stringWithFormat:@"%@searchProduct.php?method=productFilter", base] forKey:WS_ProductFilter];
    [dictServer setObject:[NSString stringWithFormat:@"%@offer.php?method=cancelOffer", base] forKey:WS_CancelOffer];
    [dictServer setObject:[NSString stringWithFormat:@"%@offer.php?method=offerMadeByme", base] forKey:WS_OfferMadeByme];
    [dictServer setObject:[NSString stringWithFormat:@"%@comments.php?method=addcomment", base] forKey:WS_Addcomment];
    [dictServer setObject:[NSString stringWithFormat:@"%@comments.php?method=deleteComment", base] forKey:WS_DeleteComment];
    [dictServer setObject:[NSString stringWithFormat:@"%@messages.php?method=addMessages", base] forKey:WS_AddMessages];
    [dictServer setObject:[NSString stringWithFormat:@"%@messages.php?method=getMessages", base] forKey:WS_GetMessages];
    [dictServer setObject:[NSString stringWithFormat:@"%@offer.php?method=getOffers", base] forKey:WS_GetOffers];
    [dictServer setObject:[NSString stringWithFormat:@"%@likes.php?method=getLikesUsers", base] forKey:WS_GetLikesUsers];
    [dictServer setObject:[NSString stringWithFormat:@"%@getContentPage.php?method=getContentPage", base] forKey:WS_GetContentPage];
    [dictServer setObject:[NSString stringWithFormat:@"%@getActivity.php?method=getActivities", base] forKey:WS_GetActivites];
    [dictServer setObject:[NSString stringWithFormat:@"%@settings.php?method=setting", base] forKey:WS_Setting];
    [dictServer setObject:[NSString stringWithFormat:@"%@likes.php?method=stuffLikes", base] forKey:WS_StuffLikes];
    [dictServer setObject:[NSString stringWithFormat:@"%@feedback.php?method=getFeedback", base] forKey:WS_GetFeedback];
    [dictServer setObject:[NSString stringWithFormat:@"%@feedback.php?method=addFeedback", base] forKey:WS_AddFeedback];
    [dictServer setObject:[NSString stringWithFormat:@"%@searchUser.php?method=searchUsers", base] forKey:WS_SearchUsers];
    [dictServer setObject:[NSString stringWithFormat:@"%@feedback.php?method=editFeedback", base] forKey:WS_EditFeedback];
    [dictServer setObject:[NSString stringWithFormat:@"%@getPromotion.php?method=getPromotion", base] forKey:WS_GetPromotion];
    [dictServer setObject:[NSString stringWithFormat:@"%@facebookCommonFriend.php?method=commonFriendList", base] forKey:WS_CommonFriendList];
    [dictServer setObject:[NSString stringWithFormat:@"%@products.php?method=addProduct", base] forKey:WS_AddProduct];
    [dictServer setObject:[NSString stringWithFormat:@"%@products.php?method=editProduct", base] forKey:WS_EditProduct];
    [dictServer setObject:[NSString stringWithFormat:@"%@products.php?method=deleteProduct", base] forKey:WS_DeleteProduct];
    [dictServer setObject:[NSString stringWithFormat:@"%@products.php?method=markAsSold", base] forKey:WS_MarkAsSold];
    [dictServer setObject:[NSString stringWithFormat:@"%@editProfile.php?method=resendMail", base] forKey:WS_ResendMail];
}

- (NSString *) getDictServer:(NSString *) key
{
    NSString * url = [dictServer objectForKey:key];
    
    if (!url)
        url = @"";
    
    return url;
}

+ (CLLocationManager *) getLoactionManager
{
    [locationManager startUpdatingLocation];
    return locationManager;
}

-(BOOL)isInternetAvailable
{
    NetworkReachability *objReach=[NetworkReachability reachabilityForInternetConnection];
    NetworkStatus status=[objReach currentReachabilityStatus];
    if (status == NotReachable) {
        return NO;
    }
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark - RemoteNotifications methods
// here a notification methods......
-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    //[[BuyerDataObject sharderInstance] setStrPushNotificationId:@""];
    NSLog(@"didFailToRegisterForRemoteNotificationsWithError");
    apnsToken = @"";
}

- (void)application:(UIApplication*) application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*) deviceToken
{
    NSString *token = [[deviceToken description] stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];
    token = [token stringByReplacingOccurrencesOfString:@" " withString:@""];
    apnsToken = [token copy];
    NSLog(@"APNS DeviceToken-%@-", apnsToken);
}

- (void)application:(UIApplication *) application didReceiveRemoteNotification:(NSDictionary *) userInfo {
    NSLog(@"userInfo = %@", userInfo);
    
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    NSString * sound = [defaults objectForKey:@"sound"];
    NSString * vibrate = [defaults objectForKey:@"vibrate"];
    NSString * light = [defaults objectForKey:@"light"];
    NSLog(@"sound--> %@ vibrate--> %@ light--> %@",sound,vibrate,light);
    if(sound.intValue == 1) {
        SystemSoundID soundID;
        NSString *soundFile = [[NSBundle mainBundle]pathForResource:@"default" ofType:@"mp3"];
        AudioServicesCreateSystemSoundID((__bridge CFURLRef)[NSURL fileURLWithPath:soundFile],&soundID);
        AudioServicesPlaySystemSound(soundID);
    }
    
    if (vibrate.intValue == 1) {
        if(isIPhone) {
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
        }
    }
    
    NSString * title = [[userInfo objectForKey:@"aps"] objectForKey:@"alert"];
    NSString * msg = [userInfo objectForKey:@"msg"];

    [[[UIAlertView alloc]initWithTitle:title message:msg delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    if(buttonIndex == 0) {
        [self performSelectorInBackground:@selector(TestThread) withObject:nil];
    }
}

- (void) TestThread
{
    if (leveyTabBar) {
        if (btnTabActivity)
            [leveyTabBar tabBarButtonClicked:btnTabActivity];
    }
}

#pragma mark - Memory management methods
-(void)clearApplicationCaches
{
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
}

// here we show tabBar Buttons on Bottom........
- (void) TabBarShow {
    BrowseScreen *VC1 = [[BrowseScreen alloc] initWithNibName:@"BrowseScreen" bundle:nil];
	FollowingViewController *VC2 = [[FollowingViewController alloc] init];
    CameraTest *VC3 = [[CameraTest alloc] init];
	ActivityViewController *VC4 = [[ActivityViewController alloc] init];
	MeViewController *VC5 = [[MeViewController alloc] init];
    [VC1 viewWillAppearClone];
    
    UINavigationController *nc1 = [[UINavigationController alloc] initWithRootViewController:VC1];
    UINavigationController *nc2 = [[UINavigationController alloc] initWithRootViewController:VC2];
	UINavigationController *nc3 = [[UINavigationController alloc] initWithRootViewController:VC3];
    UINavigationController *nc4 = [[UINavigationController alloc] initWithRootViewController:VC4];
    UINavigationController *nc5 = [[UINavigationController alloc] initWithRootViewController:VC5];
    
	nc1.delegate = self;
    nc2.delegate = self;
    nc3.delegate = self;
    nc4.delegate = self;
    nc5.delegate = self;
	
	NSArray *ctrlArr = [NSArray arrayWithObjects:nc1,nc2,nc3,nc4,nc5,nil];
	
	NSMutableDictionary *imgDic = [NSMutableDictionary dictionaryWithCapacity:3];
	[imgDic setObject:[UIImage imageNamed:@"browse_normal_icon.png"] forKey:@"Default"];
	[imgDic setObject:[UIImage imageNamed:@"browse_active_icon.png"] forKey:@"Highlighted"];
	[imgDic setObject:[UIImage imageNamed:@"browse_active_icon.png"] forKey:@"Seleted"];
	NSMutableDictionary *imgDic2 = [NSMutableDictionary dictionaryWithCapacity:3];
	[imgDic2 setObject:[UIImage imageNamed:@"following_normal_icon.png"] forKey:@"Default"];
	[imgDic2 setObject:[UIImage imageNamed:@"following_active_icon.png"] forKey:@"Highlighted"];
	[imgDic2 setObject:[UIImage imageNamed:@"following_active_icon.png"] forKey:@"Seleted"];
	NSMutableDictionary *imgDic3 = [NSMutableDictionary dictionaryWithCapacity:3];
	[imgDic3 setObject:[UIImage imageNamed:@"sell_normal_icon.png"] forKey:@"Default"];
	[imgDic3 setObject:[UIImage imageNamed:@"sell_icon_active.png"] forKey:@"Highlighted"];
	[imgDic3 setObject:[UIImage imageNamed:@"sell_icon_active.png"] forKey:@"Seleted"];
	NSMutableDictionary *imgDic4 = [NSMutableDictionary dictionaryWithCapacity:3];
	[imgDic4 setObject:[UIImage imageNamed:@"activity_normal.png"] forKey:@"Default"];
	[imgDic4 setObject:[UIImage imageNamed:@"activity_active.png"] forKey:@"Highlighted"];
	[imgDic4 setObject:[UIImage imageNamed:@"activity_active.png"] forKey:@"Seleted"];
    NSMutableDictionary *imgDic5 = [NSMutableDictionary dictionaryWithCapacity:3];
    [imgDic5 setObject:[UIImage imageNamed:@"me_normal_icon.png"] forKey:@"Default"];
    [imgDic5 setObject:[UIImage imageNamed:@"me_active_icon.png"] forKey:@"Highlighted"];
    [imgDic5 setObject:[UIImage imageNamed:@"me_active_icon.png"] forKey:@"Seleted"];
	
	NSArray *imgArr = [NSArray arrayWithObjects:imgDic,imgDic2,imgDic3,imgDic4,imgDic5,nil];
	
	leveyTabBarController = [[LeveyTabBarController alloc] initWithViewControllers:ctrlArr imageArray:imgArr];
	[leveyTabBarController.tabBar setBackgroundImage:[UIImage imageNamed:@""]];
	[leveyTabBarController setTabBarTransparent:YES];
    
    leveyTabBarController.delegate = self;
    
    [self.navigationController pushViewController:leveyTabBarController animated:NO];
    //[self.window addSubview:leveyTabBarController.view];
    //[self.window makeKeyAndVisible];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
	if ([viewController isKindOfClass:[CameraTest class]]) {
        [leveyTabBarController hidesTabBar:NO animated:YES];
	} else if ([viewController isKindOfClass:[ActivityViewController class]]) {
        [leveyTabBarController hidesTabBar:NO animated:YES];
    }  else if ([viewController isKindOfClass:[SettingViewController class]]) {
        [leveyTabBarController hidesTabBar:NO animated:YES];
    }
    
    if (viewController.hidesBottomBarWhenPushed) {
        [leveyTabBarController hidesTabBar:YES animated:YES];
    } else {
        [leveyTabBarController hidesTabBar:NO animated:YES];
    }
    
    if ([viewController isKindOfClass:[CameraTest class]]) {
        [leveyTabBarController hidesTabBar:YES animated:YES];
    }
}

#pragma mark - Calculate Date and time
// calculate date and time to convert........
- (NSString *) getTimeDiffrece:(NSString *) messageTime
{
    NSString * strTimeDifferece = @"";

    @try {
        NSDate *now = [NSDate date];
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setTimeZone:[NSTimeZone timeZoneWithName:TimeZone]];
        [formatter setDateFormat:DateFormate];
        NSString * strCurrentTime = [formatter stringFromDate:now];
        
        NSArray * arrCurrentTime = [strCurrentTime componentsSeparatedByString:@"-"];
        
        NSDateFormatter *dateFormatter1 = [[NSDateFormatter alloc] init];
        [dateFormatter1 setDateFormat:@"yyyy-MM-dd hh:mm:ss"];
        NSDate *date1 = [dateFormatter1 dateFromString: messageTime];
        formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:DateFormate];
        NSString *convertedString = [formatter stringFromDate:date1];
        
        NSArray * arrMessageTime = [convertedString componentsSeparatedByString:@"-"];
        
        long int year = [[arrCurrentTime objectAtIndex:0]intValue] - [[arrMessageTime objectAtIndex:0]intValue];
        long int months = [[arrCurrentTime objectAtIndex:1]intValue] - [[arrMessageTime objectAtIndex:1]intValue];
        long int days = [[arrCurrentTime objectAtIndex:2]intValue] - [[arrMessageTime objectAtIndex:2]intValue];
        long int hours = [[arrCurrentTime objectAtIndex:3]intValue] - [[arrMessageTime objectAtIndex:3]intValue];
        long int minites = [[arrCurrentTime objectAtIndex:4]intValue] - [[arrMessageTime objectAtIndex:4]intValue];
        long int seconds = [[arrCurrentTime objectAtIndex:5]intValue] - [[arrMessageTime objectAtIndex:5]intValue];
        
        long int totalTime = seconds + (minites * 60) + (hours * 60 * 60) + (days * 24 * 60 * 60) + (months * 30 * 24 * 60 * 60) + (year * 365 * 24 * 60 * 60);
        
        if ((int)(totalTime/(24 * 60 * 60)) == 1) {
            strTimeDifferece = [NSString stringWithFormat:@"%d day ago.", (int)(totalTime/(24 * 60 * 60))];
        } else if ((int)(totalTime/(24 * 60 * 60)) > 0) {
            strTimeDifferece = [NSString stringWithFormat:@"%d days ago.", (int)(totalTime/(24 * 60 * 60))];
        } else if ((int)(totalTime/( 60 * 60)) == 1) {
            strTimeDifferece = [NSString stringWithFormat:@"%d hour ago.", (int)(totalTime/(60 * 60))];
        } else if ((int)(totalTime/( 60 * 60)) > 0) {
            strTimeDifferece = [NSString stringWithFormat:@"%d hours ago.", (int)(totalTime/(60 * 60))];
        } else if ((int)(totalTime/(60)) == 1) {
            strTimeDifferece = [NSString stringWithFormat:@"%d minute ago.", (int)(totalTime/(60))];
        } else if ((int)(totalTime/(60)) > 0) {
            strTimeDifferece = [NSString stringWithFormat:@"%d minutes ago.", (int)(totalTime/(60))];
        } else if ((int)(totalTime/(1)) == 1) {
            strTimeDifferece = [NSString stringWithFormat:@"%d second ago.", (int)(totalTime/(1))];
        } else if ((int)(totalTime/(1)) > 0) {
            strTimeDifferece = [NSString stringWithFormat:@"%d seconds ago.", (int)(totalTime/(1))];
        } else {
            strTimeDifferece = @"a moment ago.";
        }
    } @catch (NSException *exception) {
    } @finally { }

    return strTimeDifferece;
}

#pragma mark -Facebook Login Methods
- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI {
    
    permissions = [[NSArray alloc] initWithObjects: @"publish_actions",@"email",@"user_birthday",nil];
    return [FBSession openActiveSessionWithPublishPermissions:permissions defaultAudience:FBSessionDefaultAudienceEveryone  allowLoginUI:allowLoginUI completionHandler:^(FBSession *session,FBSessionState state,NSError *error) {
        [self sessionStateChanged:session state:state error:error];
    }];
}

-(void)fbResync
{
    ACAccountStore *accountStore;
    ACAccountType *accountTypeFB;
    if ((accountStore = [[ACAccountStore alloc] init]) && (accountTypeFB = [accountStore accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierFacebook] ) ){
        
        NSArray *fbAccounts = [accountStore accountsWithAccountType:accountTypeFB];
        id account;
        if (fbAccounts && [fbAccounts count] > 0 && (account = [fbAccounts objectAtIndex:0])){
            
            [accountStore renewCredentialsForAccount:account completion:^(ACAccountCredentialRenewResult renewResult, NSError *error) {
                //we don't actually need to inspect renewResult or error.
                if (error){
                    NSLog(@"Error in my method");
                }
            }];
        }
    }
}

- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    // attempt to extract a token from the url
    return [FBSession.activeSession handleOpenURL:url];
}
- (void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error
{
    [self fbResync];
    switch (state) {
        case FBSessionStateOpen: {
            [[FBRequest requestForMe] startWithCompletionHandler:
             ^(FBRequestConnection *connection,
               NSDictionary<FBGraphUser> *user,
               NSError *error) {
                 if (error) {
                     NSLog(@"Error - %@",error);
                     //error
                 } else {;
                     [[LoadingViewController instance]startRotation];
                     [self.vcWelcome facebook:user];
                     [[LoadingViewController instance]startRotation];
                 }
             }];
        }
            break;
        case FBSessionStateClosed:
            NSLog(@"Rajat Session state closed");
            break;
        case FBSessionStateClosedLoginFailed:
            [FBSession.activeSession closeAndClearTokenInformation];
            break;
        default:
            break;
    }
    
    [[NSNotificationCenter defaultCenter]postNotificationName:FBSessionStateChangedNotification object:session];
    if (error) {
        NSString *errorTitle = NSLocalizedString(@"Error", @"Facebook connect");
        NSString *errorMessage = [error localizedDescription];
        if (error.code == FBErrorLoginFailedOrCancelled) {
            errorTitle = NSLocalizedString(@"Facebook Login Failed", @"Facebook Connect");
            errorMessage = NSLocalizedString(@"Make sure you've allowed My App to use Facebook in Settings > Facebook.", @"Facebook connect");
        }
        NSLog(@"Error - %@",error);
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:errorTitle
                                                            message:errorMessage
                                                           delegate:nil
                                                  cancelButtonTitle:NSLocalizedString(@"OK", @"Facebook Connect")
                                                  otherButtonTitles:nil];
        [alertView show];
    }
}

@end
