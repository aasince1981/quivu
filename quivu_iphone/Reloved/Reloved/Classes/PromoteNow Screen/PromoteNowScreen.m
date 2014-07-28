//
//  PromoteNowScreen.m
//  quivu
//
//  Created by Kamal on 10/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "PromoteNowScreen.h"

@interface PromoteNowScreen ()

@end

@implementation PromoteNowScreen

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
    [view_ImageBg.layer setBorderColor:[UIColor whiteColor].CGColor];
    [view_ImageBg.layer setBorderWidth:4.0f];
    view_SingleImage.frame = CGRectMake(4,4, 220, 148);
    [view_ImageBg addSubview:view_SingleImage];
    view_Defaults.frame = CGRectMake(75, 392, view_Defaults.frame.size.width, view_Defaults.frame.size.height);
    [sv_SccrollView addSubview:view_Defaults];
    boolFacebook = NO;
    boolTwitter = NO;
    boolInstagram = NO;
    [self SetFontType];
    [self performSelectorOnMainThread:@selector(GetPromotionData) withObject:nil waitUntilDone:YES];
}

- (void) SetFontType {
    lblUserName.font = FONT_Lato_Bold(14.0f);
    lblPromote.font = FONT_Lato_Bold(20.0f);
    lblPromoteOn.font = FONT_Lato_Bold(15.0f);
    lblPromoteQuivu.font = FONT_Lato_Bold(15.0f);
    lblShoppingQuivu.font = FONT_Lato_Light(13.0f);
    btnPromoteBottom.titleLabel.font = FONT_Lato_Bold(15.0f);
    btnPromoteHeader.titleLabel.font = FONT_Lato_Bold(14.0f);
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    NSString * strUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[KAppDelegate.dictLoginInfo objectForKey:@"UserImage"]];
    [obNet SetImageToView:imgUserImage fromImageUrl:strUrl Option:5];
    lblUserName.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
}

// here we get promote now images .......................
- (void) GetPromotionData {
    NSString * UserId = @"";
    @try {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@",[KAppDelegate getDictServer:WS_GetPromotion],UserId];
        NSLog(@"GetPromotion Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetPromotion Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrImages = [json objectForKey:@"Images"];
                    
                    if(arrImages.count > 0 && arrImages.count < 3) {
                        NSString * strUrl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arrImages objectAtIndex:0] objectForKey:@"ProImageName"]];
                        [obNet SetImageToView:imgFirstImage fromImageUrl:strUrl Option:5];
                        [self performSelector:@selector(imageWithView:) withObject:view_ImageBg afterDelay:2.0];
                    }
                    
                    if(arrImages.count > 2) {
                        @try {
                            NSString * strUrl1 =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arrImages objectAtIndex:0] objectForKey:@"ProImageName"]];
                            [obNet SetImageToView:imgFirst fromImageUrl:strUrl1 Option:5];
                        } @catch (NSException *exception) {}
                        
                        @try {
                            NSString * strUrl2 =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arrImages objectAtIndex:1] objectForKey:@"ProImageName"]];
                            [obNet SetImageToView:imgSecond fromImageUrl:strUrl2 Option:5];
                        } @catch (NSException *exception) {}
                        
                        @try {
                            NSString * strUrl3 =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arrImages objectAtIndex:2] objectForKey:@"ProImageName"]];
                            [obNet SetImageToView:imgThird fromImageUrl:strUrl3 Option:5];
                        } @catch (NSException *exception) {}
                        
                        view_MultipleImage.frame = CGRectMake(4,4, 220, 148);
                        [view_ImageBg addSubview:view_MultipleImage];
                        [self performSelector:@selector(imageWithView:) withObject:view_ImageBg afterDelay:2.0];
                    }
                } else {
                   // UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    //[alert show];
                }
            }
        }];
    }
}

- (void) imageWithView:(UIView *)view
{
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, view.opaque, 0.0);
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage * img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    imgCaptureImage = img;
    //return img;
}

#pragma mark - Button Actions

- (IBAction)BtnFacebook:(id)sender {
    boolFacebook = YES;
    view_Facebook.frame = CGRectMake(75, 392, view_Facebook.frame.size.width, view_Facebook.frame.size.height);
    [sv_SccrollView addSubview:view_Facebook];
}

- (IBAction)BtnTwitter:(id)sender {
    boolTwitter = YES;
    view_Twitter.frame = CGRectMake(75, 392, view_Twitter.frame.size.width, view_Twitter.frame.size.height);
    [sv_SccrollView addSubview:view_Twitter];
}

- (IBAction)BtnInstagram:(id)sender {
    boolInstagram = YES;
    view_Instagram.frame = CGRectMake(75, 392, view_Instagram.frame.size.width, view_Instagram.frame.size.height);
    [sv_SccrollView addSubview:view_Instagram];
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)BtnPromoteNow:(id)sender {
    
    if(boolFacebook == YES) {
        [self openSessionWithAllowLoginUI:YES];
    } else if (boolTwitter == YES) {
        [self PostImageOnTwitter];
    } else if (boolInstagram == YES) {
        [self shareImageWithInstagram];
    }
}

#pragma  mark - PostImages on Facebook
// here we check facebook session......................
- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI {
    
    NSArray * permissions = [[NSArray alloc] initWithObjects: @"publish_stream",nil];
    return [FBSession openActiveSessionWithPublishPermissions:permissions defaultAudience:FBSessionDefaultAudienceEveryone  allowLoginUI:YES completionHandler:^(FBSession *session,FBSessionState state,NSError *error) {[self sessionStateChanged:session state:state error:error];
    }];
     
     /*
    return [FBSession openActiveSessionWithPublishPermissions:@[@"publish_stream"]
                                              defaultAudience:FBSessionDefaultAudienceEveryone
                                                 allowLoginUI:YES
                                            completionHandler:^(FBSession *session, FBSessionState status, NSError *error) {
                                                [self sessionStateChanged:session state:status error:error];
                                            }];
     */
}

/*
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
                     
                 } else  {
                     [[FBRequest requestForMe] startWithCompletionHandler:
                      ^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
                          if (!error) {
                              // if(flagWhichFacebookPost){
                              [self PostImageOnFacebook];
                              // } else {
                              //   [self postInAppFacebook];
                              // }
                          }
                      }];
                 }
             }];
        }
//            if (!error) {
//             // We have a valid session
//             NSLog(@"User session found");
//             [self fbDidLogin];
//             NSLog(@"Permissions - %@",permissions);
//             [self.window.rootViewController dismissViewControllerAnimated:YES completion:nil];
//             }
            break;
        case FBSessionStateClosed:
            
            break;
        case FBSessionStateClosedLoginFailed:
            [FBSession.activeSession closeAndClearTokenInformation];
            if (error) {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Could not login with Facebook" message:@"Facebook login failed. Please check your Facebook settings on your phone." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alert show];
            }
            break;
        default:
            break;
    }
    
    [[NSNotificationCenter defaultCenter]postNotificationName:FBSessionStateChangedNotification object:session];
    NSLog(@"Error - %@",error);
}
*/

-(void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error
{
    if (!error && state == FBSessionStateOpen){
        NSLog(@"Session opened");
        [[FBRequest requestForMe] startWithCompletionHandler:^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
            [self PostImageOnFacebook];
        }];
        return;
    }
    if (state == FBSessionStateClosed || state == FBSessionStateClosedLoginFailed){
        NSLog(@"Session closed");
    }
    
    if (error){
        NSLog(@"Error");
        NSString *alertText;
        NSString *alertTitle;
        if ([FBErrorUtility shouldNotifyUserForError:error] == YES){
            alertTitle = @"Something went wrong";
            alertText = [FBErrorUtility userMessageForError:error];
        } else {
            if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryUserCancelled) {
                NSLog(@"User cancelled login");
            } else if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryAuthenticationReopenSession){
                alertTitle = @"Session Error";
                alertText = @"Your current session is no longer valid. Please log in again.";
            } else {
                NSDictionary *errorInformation = [[[error.userInfo objectForKey:@"com.facebook.sdk:ParsedJSONResponseKey"] objectForKey:@"body"] objectForKey:@"error"];
                alertTitle = @"Something went wrong";
                alertText = [NSString stringWithFormat:@"Please retry. \n\n If the problem persists contact us and mention this error code: %@", [errorInformation objectForKey:@"message"]];
            }
        }
        [FBSession.activeSession closeAndClearTokenInformation];
    }
}

// here we post image on facebook....................
- (void) PostImageOnFacebook {
    NSString  *message = @"hello";
    UIImage *image = [UIImage imageNamed:@"icon_fb.png"];
    UIView * viewFacebookPost = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 400, 60)];
    UIImageView * imgView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 5, 50, 50)];
    imgView.image = image;
    [viewFacebookPost addSubview:imgView];
    
    UILabel * lblMessage = [[UILabel alloc] initWithFrame:CGRectMake(70, 0, 300, 60)];
    lblMessage.text = message;
    lblMessage.numberOfLines = 3;
    lblMessage.font = [UIFont systemFontOfSize:15.0];
    [viewFacebookPost addSubview:lblMessage];
    [viewFacebookPost setBackgroundColor:[UIColor clearColor]];
    UIImage * imgPost = [[UIImage alloc] init];
    imgPost = imgCaptureImage;
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc] init];
    //[params setObject:message forKey:@"message"];
    //[params setObject:UIImagePNGRepresentation(image) forKey:@"picture.type(verysmall)"];
    [params setObject:UIImagePNGRepresentation(imgPost) forKey:@"picture"];
    // shareOnFacebook.enabled = NO; //for not allowing multiple hits
    
    [FBRequestConnection startWithGraphPath:@"/me/photos"
                                 parameters:params
                                 HTTPMethod:@"POST"
                          completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
                              if (!error) {
                                  UIAlertView *tmp = [[UIAlertView alloc]  initWithTitle:@"" message:@"SuccessFully post" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
                                  [tmp show];
                              } else {
                                  NSLog(@"error--> %@",error);
                                  UIAlertView *tmp = [[UIAlertView alloc]  initWithTitle:@"" message:@"Some error happened" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
                                  [tmp show];
                              }
                          }];
    NSMutableDictionary *variables = [NSMutableDictionary dictionaryWithCapacity:3];
}

#pragma  mark - PostImages on Twitter
- (void)PostImageOnTwitter
{
    if ([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
    {
        ACAccountStore *account = [[ACAccountStore alloc] init];
        ACAccountType *accountType = [account accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierTwitter];
        NSArray *arrayOfAccons = [account accountsWithAccountType:accountType];
        for(ACAccount *acc in arrayOfAccons)
        {
            NSLog(@"%@",acc.username);
            NSLog(@"%@",acc);
        }
        [account requestAccessToAccountsWithType:accountType withCompletionHandler:^(BOOL granted, NSError *error)
         {
             if (granted == YES) {
                 NSArray *arrayOfAccounts = [account accountsWithAccountType:accountType];
                 if ([arrayOfAccounts count] > 0) {
                     ACAccount *acct = [arrayOfAccounts objectAtIndex:0];
                     TWRequest *postRequest = [[TWRequest alloc] initWithURL:[NSURL URLWithString:@"https://upload.twitter.com/1/statuses/update_with_media.json"] parameters:nil requestMethod:TWRequestMethodPOST];
                     UIImage *img = imgCaptureImage;
                     NSData *myData = UIImagePNGRepresentation(img);
                     [postRequest addMultiPartData:myData withName:@"media" type:@"image/png"];
                     myData = [[NSString stringWithFormat:@""] dataUsingEncoding:NSUTF8StringEncoding];
                     [postRequest addMultiPartData:myData withName:@"status" type:@"text/plain"];
                     [postRequest setAccount:acct];
                     [postRequest performRequestWithHandler:^(NSData *responseData, NSHTTPURLResponse *urlResponse, NSError *error)
                      {
                          if(error) {
                              UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"Error in posting" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                              [alert show];
                          } else {
                              NSLog(@"Twitter response, HTTP response: %li", (long)[urlResponse statusCode]);
                          }
                      }];
                 } else {
                     UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"You have no twitter account" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                     [alert show];
                 }
             } else {
                 UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"Permission not granted" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                 [alert show];
             }
         }];
        //  [widgetsHandler closeWidget:nil];
    } else {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"You have no twitter account" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
}

#pragma mark - Instagram Image Share Code here
-(void) shareImageWithInstagram
{
    if([KAppDelegate isInternetAvailable]) {
        [self storeimage];
        NSURL *instagramURL = [NSURL URLWithString:@"instagram://app"];
        if ([[UIApplication sharedApplication] canOpenURL:instagramURL]) {
            CGRect rect = CGRectMake(0 ,0 , 612, 612);
            NSString  *jpgPath = [NSHomeDirectory() stringByAppendingPathComponent:@"Documents/15717.ig"];
            
            NSURL *igImageHookFile = [[NSURL alloc] initWithString:[[NSString alloc] initWithFormat:@"file://%@", jpgPath]];
            _dic.UTI = @"com.instagram.photo";
            _dic.delegate=self;
            _dic = [self setupControllerWithURL:igImageHookFile usingDelegate:self];
            _dic=[UIDocumentInteractionController interactionControllerWithURL:igImageHookFile];
            _dic.delegate=self;
            [_dic presentOpenInMenuFromRect: rect    inView: self.view animated: YES ];
        } else {
            UIAlertView *errorToShare = [[UIAlertView alloc] initWithTitle:@"Instagram unavailable " message:@"You need to install Instagram in your device in order to share this image" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            errorToShare.tag=3010;
            [errorToShare show];
        }
    } else {
        [[[UIAlertView alloc]initWithTitle:@"" message:kCouldnotconnect delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
    }
}

- (void) storeimage
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,     NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *savedImagePath = [documentsDirectory stringByAppendingPathComponent:@"15717.ig"];
    UIImage *NewImg=[self resizedImage:imgCaptureImage :CGRectMake(0, 0, 612, 612) ];
    NSData *imageData = UIImagePNGRepresentation(NewImg);
    [imageData writeToFile:savedImagePath atomically:NO];
}

-(UIImage*) resizedImage:(UIImage *)inImage: (CGRect) thumbRect
{
    CGImageRef imageRef = [inImage CGImage];
    CGImageAlphaInfo alphaInfo = CGImageGetAlphaInfo(imageRef);
    if (alphaInfo == kCGImageAlphaNone)
        alphaInfo = kCGImageAlphaNoneSkipLast;
    CGContextRef bitmap = CGBitmapContextCreate(NULL,thumbRect.size.width,thumbRect.size.height,CGImageGetBitsPerComponent(imageRef),4 * thumbRect.size.width,CGImageGetColorSpace(imageRef),alphaInfo);
    CGContextDrawImage(bitmap, thumbRect, imageRef);
    CGImageRef  ref = CGBitmapContextCreateImage(bitmap);
    UIImage*    result = [UIImage imageWithCGImage:ref];
    CGContextRelease(bitmap);
    CGImageRelease(ref);
    return result;
}

#pragma mark - UIDocumentInteractionController Delegate Method
- (UIDocumentInteractionController *) setupControllerWithURL: (NSURL*) fileURL usingDelegate: (id <UIDocumentInteractionControllerDelegate>) interactionDelegate
{
    UIDocumentInteractionController *interactionController = [UIDocumentInteractionController interactionControllerWithURL:fileURL];
    interactionController.delegate = self;
    return interactionController;
}

- (void)documentInteractionControllerWillPresentOpenInMenu:(UIDocumentInteractionController *)controller{
}

- (BOOL)documentInteractionController:(UIDocumentInteractionController *)controller canPerformAction:(SEL)action{
    return YES;
}

- (BOOL)documentInteractionController:(UIDocumentInteractionController *)controller performAction:(SEL)action{
    return YES;
}

- (void)documentInteractionController:(UIDocumentInteractionController *)controller didEndSendingToApplication:(NSString *)application{
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
