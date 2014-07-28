//
//  FriendListScreen.m
//  quivu
//
//  Created by Kamal on 12/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FriendListScreen.h"

@interface FriendListScreen ()

@end

@implementation FriendListScreen

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
    tbl_FriendList.dataSource = self;
    tbl_FriendList.delegate = self;
    arrFacebookFriends = [[NSMutableArray alloc]init];
}

#pragma mark - viewWillAppear method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self openSessionWithAllowLoginUI:YES];
}

// here we check session state of facebook........
- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI {

   NSArray * permissions = [[NSArray alloc] initWithObjects:@"publish_actions",@"user_friends",nil];
    
    return [FBSession openActiveSessionWithPublishPermissions:permissions defaultAudience:FBSessionDefaultAudienceEveryone  allowLoginUI:allowLoginUI completionHandler:^(FBSession *session,FBSessionState state,NSError *error) {[self sessionStateChanged:session state:state error:error];
    }];

    /*
    return [FBSession openActiveSessionWithPublishPermissions:@[@"publish_actions",@"manage_friendlists",@"public_profile",@"user_friends"]
                                              defaultAudience:FBSessionDefaultAudienceEveryone
                                                 allowLoginUI:YES
                                            completionHandler:^(FBSession *session, FBSessionState status, NSError *error) {
                                                [self sessionStateChanged:session state:status error:error];
                                            }];
     */
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
                     [self getFacebookFriends];
                     //[self getFriends];
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
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:errorTitle message:errorMessage delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", @"Facebook Connect") otherButtonTitles:nil];
        [alertView show];
    }
//    if (error) {
//     UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Please authorize the application " delegate:nil        cancelButtonTitle:@"OK" otherButtonTitles:nil];
//     [alertView show];
//     }
    
}

/////////////////////////////////////////////////////////////////////////

/*
-(void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error
{
    // If the session was opened successfully
    if (!error && state == FBSessionStateOpen){
        NSLog(@"Session opened");
        // Show the user the logged-in UI
        [[FBRequest requestForMe] startWithCompletionHandler:^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
            
            //NSLog(@"%@",user);
            //NSLog(@"email::: %@",[user objectForKey:@"email"]);
            
            [self getFacebookFriends];
        }];
        return;
    }
    if (state == FBSessionStateClosed || state == FBSessionStateClosedLoginFailed){
        // If the session is closed
        NSLog(@"Session closed");
    }
    
    // Handle errors
    if (error){
        NSLog(@"Error");
        NSString *alertText;
        NSString *alertTitle;
        // If the error requires people using an app to make an action outside of the app in order to recover
        if ([FBErrorUtility shouldNotifyUserForError:error] == YES){
            alertTitle = @"Something went wrong";
            alertText = [FBErrorUtility userMessageForError:error];
            //[self showMessage:alertText withTitle:alertTitle];
        } else {
            
            if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryUserCancelled) {
                NSLog(@"User cancelled login");
                
            } else if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryAuthenticationReopenSession){
                alertTitle = @"Session Error";
                alertText = @"Your current session is no longer valid. Please log in again.";
                //[self showMessage:alertText withTitle:alertTitle];
                
            } else {
                NSDictionary *errorInformation = [[[error.userInfo objectForKey:@"com.facebook.sdk:ParsedJSONResponseKey"] objectForKey:@"body"] objectForKey:@"error"];
                
                alertTitle = @"Something went wrong";
                alertText = [NSString stringWithFormat:@"Please retry. \n\n If the problem persists contact us and mention this error code: %@", [errorInformation objectForKey:@"message"]];
                //[self showMessage:alertText withTitle:alertTitle];
            }
        }
        // Clear this token
        [FBSession.activeSession closeAndClearTokenInformation];
    }
}
*/

// here we get facebook friends list..........
-(void) getFacebookFriends {
    
    [[LoadingViewController instance]startRotation];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:@"id,name,picture",@"fields",nil];
    
    [FBRequestConnection startWithGraphPath:@"me/friends"
                                 parameters:params
                                 HTTPMethod:@"GET"
                          completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
                              if(error == nil) {
                                  [[LoadingViewController instance]stopRotation];
                                  FBGraphObject *response = (FBGraphObject*)result;
                                  NSLog(@"Friends: %@",[response objectForKey:@"data"]);
                                  
                                  arrFacebookFriends = [response objectForKey:@"data"];
                                  
                                  if(arrFacebookFriends.count > 0) {
                                      [self GetCommonFriendList];
                                  }
                              }
                          }];
    
    /*
    //[FBSession openActiveSessionWithAllowLoginUI: YES];
   
    ACAccountStore *accountStore = [[ACAccountStore alloc] init];
    __block ACAccount *facebookAccount = nil;
    
    ACAccountType *facebookAccountType = [accountStore accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierFacebook];
    
    NSDictionary *options = @{
                              ACFacebookAppIdKey:@"473112902823855",
                              ACFacebookPermissionsKey: @[@"publish_actions",@"user_friends"],
                              ACFacebookAudienceKey: ACFacebookAudienceFriends
                              };
    
    [accountStore requestAccessToAccountsWithType:facebookAccountType
                                          options:options completion:^(BOOL granted, NSError *error)
     {
         if (granted)
         {
             NSArray *accounts = [accountStore accountsWithAccountType:facebookAccountType];
             
             facebookAccount = [accounts lastObject];
         }
         else {
             NSLog(@"error.localizedDescription======= %@", error.localizedDescription);
             //[self performSelectorOnMainThread:@selector(showAlert) withObject:nil waitUntilDone:YES];
         }
     }];
//     [FBSession sessionOpenWithPermissions:[NSArray arrayWithObjects:@"publish_stream", nil]
//     completionHandler:
//     ^(FBSession *session,
//     FBSessionState status,
//     NSError *error)
 
    NSArray *accounts = [accountStore accountsWithAccountType:facebookAccountType];
    facebookAccount = [accounts lastObject];
    //NSString *acessToken = [NSString stringWithFormat:@"%@",facebookAccount.credential.oauthToken];
    //NSDictionary *parameters = @{@"access_token": acessToken};
    
    NSDictionary *param=[NSDictionary dictionaryWithObjectsAndKeys:@"picture,id,name,link,gender,last_name,first_name",@"fields", nil];
    
    NSURL *feedURL = [NSURL URLWithString:@"https://graph.facebook.com/me/friends"];
    SLRequest *feedRequest = [SLRequest
                              requestForServiceType:SLServiceTypeFacebook
                              requestMethod:SLRequestMethodGET
                              URL:feedURL
                              parameters:param];
    feedRequest.account = facebookAccount;
    [feedRequest performRequestWithHandler:^(NSData *responseData,
                                             NSHTTPURLResponse *urlResponse, NSError *error)
     {
         if(!error)
         {
             id json =[NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
             
             NSLog(@"Dictionary contains data: %@", json );
             [[LoadingViewController instance]stopRotation];
             arrFacebookFriends = [json objectForKey:@"data"];
             
             if(arrFacebookFriends.count > 0) {
                 [self GetCommonFriendList];
             }
             
             if([json objectForKey:@"error"]!=nil) {
                 //[self attemptRenewCredentials];
             }
             dispatch_async(dispatch_get_main_queue(),^{
                 //nameLabel.text = [json objectForKey:@"username"];
                 //[tblContacts reloadData];
             });
         }
         else{
             //handle error gracefully
             NSLog(@"error from get%@",error);
             //attempt to revalidate credentials
         }
     }];
    //[tblContacts reloadData];
    */
}

// here we call to facebook friends commen list...........
- (void) GetCommonFriendList {
    
    NSMutableString *FriendsId = [[NSMutableString alloc]init];
    
    for (int i=0; i<[arrFacebookFriends count]; i++) {
        NSDictionary * dict = [arrFacebookFriends objectAtIndex:i];
        [FriendsId appendString:[NSString stringWithFormat:@"%@,",[dict objectForKey:@"id"]]];
    }
    
    if(FriendsId.length>0) {
        [FriendsId deleteCharactersInRange:(NSRange){[FriendsId length] - 1, 1}];
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&FriendIds=%@",[KAppDelegate getDictServer:WS_CommonFriendList],[KAppDelegate.dictLoginInfo objectForKey:@"UserId"],FriendsId];
        NSLog(@"commonFriendList Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"commonFriendList Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrFacebookFriends = [json objectForKey:@"UserList"];
                    if(IS_IPHONE_5) {
                        [view_PopUp removeFromSuperview];
                    } else {
                        [view_PopUp removeFromSuperview];
                    }
                    
                    [tbl_FriendList reloadData];
                    
                } else {
                    arrFacebookFriends = [[NSMutableArray alloc]init];
                    if(IS_IPHONE_5) {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 518);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    } else {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 430);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    }
                    [tbl_FriendList reloadData];
                }
            }
        }];
    }
}

#pragma mark - Button TableView DataSource and Delegate methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrFacebookFriends.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * strCell = @"cell";
    FriendListCustomCell * cell = (FriendListCustomCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"FriendListCustomCell" owner:self options:nil] objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrFacebookFriends objectAtIndex:indexPath.row];
    
    NSString * userId = [dict objectForKey:@"UserId"];
    NSString * loginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    if(userId.intValue == loginUserId.intValue) {
        cell.btnFollow.hidden = YES;
    } else {
        cell.btnFollow.hidden = NO;
    }
    
    cell.lblName.text = [dict objectForKey:@"UserName"];
    
    NSString * imgUrl = [NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"UserImage"]];
    [cell.img_ImageView GetNSetUIImage:imgUrl DefaultImage:@"default_user.png"];
    
    NSString * FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowerStatus"] intValue]];
    
    if(FollowStatus.intValue == 1) {
        [cell.btnFollow setTitle:@"Following" forState:UIControlStateNormal];
    } else {
        [cell.btnFollow setTitle:@"Follow" forState:UIControlStateNormal];
    }
    
    cell.delegate = self;
    cell.getIndex = indexPath.row;
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 67.0f;
}

// here follow and unfollow to friend..........
- (void) FollowAndUnFollowFriends:(int)indexValue {
    NSLog(@"%d",indexValue);
    
    NSDictionary * dict = [arrFacebookFriends objectAtIndex:indexValue];
    
    NSString * FromUserId =@"";
    NSString * FromUserName =@"";
    NSString * FromUserImage =@"";
    NSString * ToUserId =@"";
    NSString * ToUserName =@"";
    NSString * ToUserImage =@"";
    NSString * Status = @"";
    
    NSString  *FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowerStatus"] intValue]];
    if(FollowStatus.intValue == 1) {
        Status = @"0";
    } else {
        Status = @"1";
    }
    
    if(KAppDelegate.dictLoginInfo.count > 0){
        @try {
            FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
        } @catch (NSException *exception) { }
        
        @try {
            FromUserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        } @catch (NSException *exception) { }
        
        @try {
            if([KAppDelegate.dictLoginInfo objectForKey:@"UserImage"] != nil) {
                FromUserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
            } else {
                FromUserImage = @"";
            }
        } @catch (NSException *exception) { }
    }
    
    if(dict.count > 0) {
        @try {
            ToUserId = [dict objectForKey:@"UserId"];
        } @catch (NSException *exception) {}
        
        @try {
            ToUserName = [dict objectForKey:@"UserName"];
        } @catch (NSException *exception) {}
        
        @try {
            ToUserImage = [dict objectForKey:@"UserImage"];
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FromUserId=%@&FromUserName=%@&FromUserImage=%@&ToUserId=%@&ToUserName=%@&ToUserImage=%@&FollowStatus=%@",[KAppDelegate getDictServer:WS_AddFollow],FromUserId,FromUserName,FromUserImage,ToUserId,ToUserName,ToUserImage,Status];
        NSLog(@"AddFollow Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddFollow Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    
                    @try {
                        NSMutableArray * arr = [[NSMutableArray alloc]init];
                        arr = [arrFacebookFriends mutableCopy];
                        NSMutableDictionary * ddd = [[NSMutableDictionary alloc]init];
                        
                        ddd = [dict mutableCopy];
                        [ddd setObject:Status forKey:@"FollowerStatus"];
                        [arr replaceObjectAtIndex:indexValue withObject:ddd];
                        arrFacebookFriends = arr;
                        [tbl_FriendList reloadData];
                    } @catch (NSException *exception) {}
                }
            }
        }];
    }
}

#pragma mark - Button Actions
- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
