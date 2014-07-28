//
//  WelcomeScreenViewController.m
//  Reloved
//
//  Created by Kamal on 09/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "WelcomeScreenViewController.h"
#import "RegistrationViewController.h"
#import "LoginScreenViewController.h"
#import "JSON.h"

@interface WelcomeScreenViewController ()

@end

@implementation WelcomeScreenViewController
@synthesize webview,isLogin,isReader;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    gender = @"";
    DeviceType = @"";
    Type = @"";
    UserSocialId = @"";
    AccessToken = @"";
    
    lblWelcome.font = FONT_Lato_Bold(20.0f);
    lblRecommend.textColor = [UIColor darkGrayColor];
    lblRecommend.font = FONT_Lato_Bold(14.0f);
    lblPost.textColor = [UIColor blackColor];
    lblPost.font = FONT_Lato_Light(12.0f);
    lblAlreadySignUP.font = FONT_Lato_Bold(15.0f);
    lblOr.font = FONT_Lato_Bold(20.0f);
    BtnLoginNow.titleLabel.font = FONT_Lato_Bold(16.0f);
    
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self BaseUrl];
}

// here we call baseUrl..............
- (void) BaseUrl {
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

// here we get cityName.....................
- (void) GetCityName
{
    locationManager = [AppDelegate getLoactionManager];
    [locationManager startUpdatingLocation];
    KAppDelegate.Latitude = [NSString stringWithFormat:@"%f", locationManager.location.coordinate.latitude];
    KAppDelegate.longitude = [NSString stringWithFormat:@"%f", locationManager.location.coordinate.longitude];
    
    //double latdouble = [KAppDelegate.Latitude doubleValue];
    //double londouble = [KAppDelegate.Longitude doubleValue];
    
    if([obNet InternetStatus:YES]) {
        NSString * url = [NSString stringWithFormat:@"http://maps.google.com/maps/api/geocode/json?latlng=%@,%@&output=json&sensor=false", KAppDelegate.Latitude, KAppDelegate.Longitude];
        NSLog(@"Google Api-%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"Google Api json value-->%@", json);
            if (json != nil) {
                if ([[json objectForKey:@"status"] isEqualToString:@"OK"]) {
                    if([obNet isObject:[json objectForKey:@"results"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                        NSArray * arr = [json objectForKey:@"results"];
                        @try {
                            NSDictionary * dd = [arr objectAtIndex:4];
                            NSArray * arrr = [dd objectForKey:@"address_components"];
                            NSLog(@"%@",arrr);
                            KAppDelegate.CityName = [[arrr objectAtIndex:0] objectForKey:@"long_name"];
                            NSLog(@"CityName %@",KAppDelegate.CityName);
                        } @catch (NSException *exception) { }
                    }
                }
            }
        }];
    }
}

#pragma mark - Button actions
- (IBAction)BtnSignUpWithEmail:(id)sender {
    static NSInteger dir = 0;
    RegistrationViewController * vc = [[RegistrationViewController alloc]initWithNibName:@"RegistrationViewController" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)BtnConnectWithFacebook:(id)sender {
//    AppDelegate *appDelegate =[[UIApplication sharedApplication] delegate];
//    [appDelegate openSessionWithAllowLoginUI:YES];

    locationManager = [AppDelegate getLoactionManager];
    if([obNet InternetStatus:YES]){
        if (locationManager.location.coordinate.latitude == 0 && locationManager.location.coordinate.longitude == 0) {            [[[UIAlertView alloc] initWithTitle:@"" message:@"Application is not able to fetch your current location coordinates!" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
        } else {
            
            [self GetCityName];
            AppDelegate *appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
            [appDelegate openSessionWithAllowLoginUI:YES];
            
            //            fb=[[Facebook alloc]initWithAppId:KFBAppID andDelegate:self];
            //            [fb logout];
            //            [fb setSessionDelegate:self];
            //            if (![fb isSessionValid]) {
            //                NSArray *permissions = [[NSArray alloc] initWithObjects:@"publish_stream",@"offline_access",@"email",nil];
            //                [fb authorize:permissions];
            //            }
        }
    }
}

- (IBAction)BtnLoginNow:(id)sender {
    
    static NSInteger dir = 0;
    LoginScreenViewController * login = [[LoginScreenViewController alloc]initWithNibName:@"LoginScreenViewController" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    login.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:login animated:YES];
}

- (IBAction)btnSignInWithGoogle:(id)sender
{
    locationManager = [AppDelegate getLoactionManager];
    
    if([obNet InternetStatus:YES]) {
        if (locationManager.location.coordinate.latitude == 0 && locationManager.location.coordinate.longitude == 0) {
            [[[UIAlertView alloc] initWithTitle:@"" message:@"Application is not able to fetch your current location coordinates!" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
        } else {
            [self GetCityName];
            webview.delegate = self;
            NSString *url = [NSString stringWithFormat:@"https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%@&redirect_uri=%@&scope=%@&data-requestvisibleactions=%@",KClientID,Callbakc,ScopeUrl,Visibleactions];
            View_Webview.frame = self.view.frame;
            
            [webview loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
            [self.view addSubview:View_Webview];
        }
    }
}

- (IBAction)BtnCross:(id)sender {
    [View_Webview removeFromSuperview];
}

#pragma mark - facebook Integration Implementation
// here we get user facebook details.........................
- (void) facebook:(id) dic {
    
    NSLog(@"FBRequest User dict = %@",dic);
    
    //[self registerFacebookUser:dic];
    
    DeviceType = @"2";
    Type = @"2";
    
    NSString * UserEmail = nil;
    if([dic objectForKey:@"email"] != nil) {
        UserEmail=[dic objectForKey:@"email"];
    } else {
        UserEmail=@"";
    }
    
    NSString * userName = nil;
    if([dic objectForKey:@"name"]) {
        userName=[dic objectForKey:@"name"];
    } else {
        userName=@"";
    }
    
    NSString * UserFirstName = nil;
    if([dic objectForKey:@"first_name"]) {
        UserFirstName=[dic objectForKey:@"first_name"];
    } else {
        UserFirstName=@"";
    }
    
    NSString * UserLastName = nil;
    if([dic objectForKey:@"last_name"]) {
        UserLastName=[dic objectForKey:@"last_name"];
    } else {
        UserLastName=@"";
    }
    
    NSString * birthDate = nil;
    if([dic objectForKey:@"birthday"]) {
        NSDateFormatter *dateFormatter=[[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"MM/dd/yyyy"];
        
        NSDate *temp=[dateFormatter dateFromString:[dic objectForKey:@"birthday"]];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        birthDate=[dateFormatter stringFromDate:temp];
    } else {
        birthDate=@"";
    }
    
    if([dic objectForKey:@"gender"]) {
        if([[dic objectForKey:@"gender"] isEqualToString:@"male"]){
            gender=@"1";
        }else{
            gender=@"0";
        }
    } else {
        gender=@"";
    }
    
    if([dic objectForKey:@"id"]) {
        UserSocialId=[dic objectForKey:@"id"];
    } else {
        UserSocialId=@"";
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserSocialId=%@&UserName=%@&UserFirstName=%@&UserLastName=%@&UserEmailAddress=%@&UserCity=%@&UserLatitude=%@&UserLongitude=%@&UserNotificationId=%@&UserAppVersion=%@&UserDeviceId=%@&UserDefaultCity=%@&Type=%@&UserDeviceType=%@&UserGender=%@",[KAppDelegate getDictServer:WS_UserRegistrations],UserSocialId,userName,UserFirstName,UserLastName,UserEmail,@"",KAppDelegate.Latitude,KAppDelegate.Longitude,KAppDelegate.apnsToken,AppVersion,Device_IMEI,KAppDelegate.CityName,Type,DeviceType,gender];
        NSLog(@"UserRegistrations Facebbok Url-->%@",url);
        
        @try {
            defaults = [NSUserDefaults standardUserDefaults];
            [defaults setObject:UserSocialId forKey:FACEBOOKID];
            [defaults synchronize];
        } @catch (NSException *exception) { }
        
        [self LoginUsingFacebook:url];
    }
}

- (void) LoginUsingFacebook:(NSString *) url
{
    NSString * backUpURL = [NSString stringWithFormat:@"%@", url];
    url = [NSString stringWithFormat:@"%@",url];
    [self UploadImage:url BackUpURL:backUpURL];
}

// here we upload facebook profile image to server.................
- (void) UploadImage:(NSString *) urlString BackUpURL:(NSString *) backUpURL {
    
    if([obNet isObject:[defaults objectForKey:FACEBOOKID] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        NSLog(@"urlString-%@", urlString);
        [self performSelectorInBackground:@selector(StartLoader) withObject:nil];
        NSString * facebookImageURL = [NSString stringWithFormat:@"http://graph.facebook.com/%@/picture?type=large", [defaults objectForKey:FACEBOOKID]];
        NSLog(@"facebookImageURL-%@", facebookImageURL);
        NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:facebookImageURL]];
        [self UploadFileWithFileData:imageData FileName:@"kamal.jpg" AndWebService:urlString BackUpURL:backUpURL];
    } else {
        NSLog(@"Facebook Id - %@",[defaults objectForKey:FACEBOOKID]);
        NSLog(@"Facebook Id not available.");
    }
}

- (void)UploadFileWithFileData:(NSData *) fileData FileName:(NSString *) fileName AndWebService:(NSString *) webservice BackUpURL:(NSString *) backUpURL {
    @try {
        webservice = [webservice stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSLog(@"UploadFileWithFileData-%lu-%@", (unsigned long)fileData.length, webservice);
        
        NSURL *url = [NSURL URLWithString:webservice];
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc]initWithURL:url] ;
        request.HTTPMethod = @"POST";
        
        NSString *boundary = @"0xKhTmLbOuNdArY";
        NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary, nil];
        [request addValue:contentType forHTTPHeaderField:@"Content-Type"];
        
        NSMutableData *body = [NSMutableData data];
        [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"UserImage\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:fileData];
        [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [request setHTTPBody:body];
        
        [self performSelectorInBackground:@selector(sendSynchronousRequest:) withObject:[NSArray arrayWithObjects:request, backUpURL, nil]];
    } @catch (NSException *exception) {
    }
}

- (void) StartLoader
{
    [[LoadingViewController instance] startRotation];
}

- (void) sendSynchronousRequest:(NSArray *) array
{
    NSMutableURLRequest * request = [array objectAtIndex:0];
    NSString * backUpURL = [array objectAtIndex:1];
    @try {
        NSURLResponse *response;
        NSData *POSTReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
        NSString *theReply = [[NSString alloc] initWithBytes:[POSTReply bytes] length:[POSTReply length] encoding: NSASCIIStringEncoding];
        
        NSError * e;
        NSMutableDictionary *dictJSON = [NSJSONSerialization JSONObjectWithData: [theReply dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
        NSLog(@"UploadFileWithFileDataJSON theReply = %@", theReply);
        NSLog(@"UploadFileWithFileDataJSON dictJSON = %@", dictJSON);
        
        if (dictJSON != nil) {
            if ([dictJSON isKindOfClass:[NSDictionary class]]) {
                if (dictJSON != nil) {
                    if ([[dictJSON objectForKey:@"success"] intValue] == 1) {
                        [View_Webview removeFromSuperview];
                        KAppDelegate.dictLoginInfo = [dictJSON objectForKey:@"UserInformation"];
                        
                        @try {
                            [obNet setDefaultUserData:KAppDelegate.dictLoginInfo WithKey:Key_LoginDetail];
                        } @catch (NSException *exception) {}
                        
                        [KAppDelegate TabBarShow];
                        
                    } else {
                        [[[UIAlertView alloc] initWithTitle:@"" message:[dictJSON objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                    }
                }
            }
        }
        [[LoadingViewController instance] stopRotation];
    } @catch (NSException *exception) {
        [[LoadingViewController instance] stopRotation];
    }
}

#pragma mark - GooglePlus Integratio
// here is a google Plus integration........................
- (void) webViewDidStartLoad:(UIWebView *)webView {
    [[LoadingViewController instance] startRotation];
}
- (void) webViewDidFinishLoad:(UIWebView *)webView {
    [[LoadingViewController instance] stopRotation];
}

- (void) webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    NSLog(@"error-->%@",error);
}

- (BOOL)webView:(UIWebView*)webView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType {
    //    [indicator startAnimating];
    if ([[[request URL] host] isEqualToString:@"localhost"]) {
        [[LoadingViewController instance] startRotation];
        // Extract oauth_verifier from URL query
        NSString* verifier = nil;
        NSArray* urlParams = [[[request URL] query] componentsSeparatedByString:@"&"];
        for (NSString* param in urlParams) {
            NSArray* keyValue = [param componentsSeparatedByString:@"="];
            NSString* key = [keyValue objectAtIndex:0];
            if ([key isEqualToString:@"code"]) {
                verifier = [keyValue objectAtIndex:1];
                NSLog(@"verifier %@",verifier);
                break;
            }
        }
        
        if (verifier) {
            NSString *data = [NSString stringWithFormat:@"code=%@&client_id=%@&client_secret=%@&redirect_uri=%@&grant_type=authorization_code", verifier,KClientID,KSecretId,Callbakc];
            
            NSString *url = [NSString stringWithFormat:@"https://accounts.google.com/o/oauth2/token"];
            
            NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
            [request setHTTPMethod:@"POST"];
            [request setHTTPBody:[data dataUsingEncoding:NSUTF8StringEncoding]];
            
            NSLog(@"request-->%@",request);
            NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:request delegate:self];
            receivedData = [[NSMutableData alloc] init];
            
        } else {
            // ERROR!
        }
        
        [webView removeFromSuperview];
        
        return NO;
    }
    return YES;
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [receivedData appendData:data];
    NSLog(@"verifier %@",receivedData);
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                    message:[NSString stringWithFormat:@"%@", error]
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    NSString *response = [[NSString alloc] initWithData:receivedData encoding:NSUTF8StringEncoding];
    NSString * responese = [response JSONValue];
    
    NSLog(@"responese--->%@",responese);
    
    SBJsonParser *jResponse = [[SBJsonParser alloc]init];
    NSDictionary *tokenData = [jResponse objectWithString:response];
    
    NSString *pdata = [NSString stringWithFormat:@"type=%@&token=%@&secret=%@&login=%@",[tokenData objectForKey:@"token_type"] ,[tokenData objectForKey:@"refresh_token"], KSecretId,self.isLogin];
    
    AccessToken = [tokenData objectForKey:@"access_token"];
    [self GetUserData];
}

// here we get userData .......................
- (void)GetUserData {
    
    NSError * error;
    
    NSString *str=[NSString stringWithFormat:@"https://www.googleapis.com/oauth2/v1/userinfo?access_token=%@",AccessToken];
    NSString *escapedUrl = [str stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@",escapedUrl]];
    NSString *jsonData = [[NSString alloc] initWithContentsOfURL:url usedEncoding:nil error:nil];
    [[LoadingViewController instance] stopRotation];
    NSMutableDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:[jsonData dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:&error];
    
    [self registerGooglePlusUser:jsonDictionary];
    
    NSLog(@" ujsonDictionary %@",jsonDictionary);
}

-(void)registerGooglePlusUser:(NSMutableDictionary *)dict {
    
    NSLog(@"Google Details dict-->%@",dict);
    
    NSString * userName = @"";
    NSString * UserEmail = @"";
    NSString * UserGender = @"";
    NSString * ImageUrl = @"";
    
    if([obNet isObject:dict String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        DeviceType = @"2";
        Type = @"3";
        
        @try {
            userName = [dict objectForKey:@"name"];
        } @catch (NSException *exception) { }
        
        @try {
            UserEmail = [dict objectForKey:@"email"];
        } @catch (NSException *exception) { }
        
        @try {
            UserSocialId = [dict objectForKey:@"id"];
        } @catch (NSException *exception) { }
        
        @try {
            UserGender = [dict objectForKey:@"gender"];
            
            if([UserGender isEqualToString:@"male"]) {
                gender = @"1";
            } else {
                gender = @"0";
            }
        } @catch (NSException *exception) {}
        
        @try {
            ImageUrl = [dict objectForKey:@"picture"];
        } @catch (NSException *exception) { }
        
        
        if([obNet InternetStatus:YES]) {
            NSString *url = [NSString stringWithFormat:@"%@&UserSocialId=%@&UserName=%@&UserFirstName=%@&UserLastName=%@&UserEmailAddress=%@&UserCity=%@&UserLatitude=%@&UserLongitude=%@&UserNotificationId=%@&UserAppVersion=%@&UserDeviceId=%@&UserDefaultCity=%@&Type=%@&UserDeviceType=%@&UserGender=%@",[KAppDelegate getDictServer:WS_UserRegistrations],UserSocialId,userName,@"",@"",UserEmail,@"",KAppDelegate.Latitude,KAppDelegate.Longitude,KAppDelegate.apnsToken,AppVersion,Device_IMEI,KAppDelegate.CityName,Type,DeviceType,gender];
            NSLog(@"UserRegistrations GooglePlus Url-->%@",url);
            
            NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:ImageUrl]];
            [self UploadFileWithFileData:imageData FileName:@"kamal.jpg" AndWebService:url BackUpURL:url];
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
