//
//  FindAndInviteFriends.m
//  Reloved
//
//  Created by Kamal on 30/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FindAndInviteFriends.h"
#import "ItemListingViewController.h"
#import "ContactsViewController.h"
#import "UserSearchScreen.h"
#import "PromoteNowScreen.h"
#import "FriendListScreen.h"

@interface FindAndInviteFriends ()

@end

@implementation FindAndInviteFriends

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
    
    [self SetFontType];
    
    sv_Scrollview.delegate = self;
    sv_Scrollview.contentSize = CGSizeMake(320,800);
}

- (void) SetFontType {
    btnSearchByUserName.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnRecommendedUsers.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnPromoteQuivu.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnFindFriendsFacebook.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnInviteFriendsFacebook.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnContacts.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnShoutOutFacebook.titleLabel.font = FONT_Lato_Bold(16.0f);
    btnTwitter.titleLabel.font = FONT_Lato_Bold(16.0f);
    
    lblFindFriends.font = FONT_Lato_Bold(20.0f);
    lblFindFriends.font = FONT_Lato_Bold(15.0f);
    lblInviteFriends.font = FONT_Lato_Bold(15.0f);
    lblShoutOut.font = FONT_Lato_Bold(15.0f);
    lblLovingUsingQuivu.font = FONT_Lato_Bold(15.0f);
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - Button Actions

- (IBAction)btnSearchByUserName:(id)sender {
    static NSInteger dir = 0;
    UserSearchScreen * search = [[UserSearchScreen alloc]initWithNibName:@"UserSearchScreen" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    search.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:search animated:YES];
}

- (IBAction)btnFindFriendsFacebook:(id)sender {
    static NSInteger dir = 0;
    FriendListScreen * friend = [[FriendListScreen alloc]initWithNibName:@"FriendListScreen" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    friend.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:friend animated:YES];
}

- (IBAction)btnInviteFriendsFacebook:(id)sender {
    [self openSessionWithAllowLoginUI:YES];
}


// here we send facebook notification to app friends......
- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI {
    
    NSArray *permissions = [[NSArray alloc] initWithObjects: @"publish_actions",@"email",@"user_birthday",nil];
    return [FBSession openActiveSessionWithPublishPermissions:permissions defaultAudience:FBSessionDefaultAudienceEveryone  allowLoginUI:allowLoginUI completionHandler:^(FBSession *session,FBSessionState state,NSError *error) {[self sessionStateChanged:session state:state error:error];
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
                     [self sendinvitation];
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

-(void)sendinvitation
{
    [FBWebDialogs
     presentRequestsDialogModallyWithSession:nil
     message:@"Learn how to make your iOS apps social."
     title:nil
     parameters:nil
     handler:^(FBWebDialogResult result, NSURL *resultURL, NSError *error) {
         if (error) {
             // Error launching the dialog or sending the request.
             NSLog(@"Error sending request.");
         } else {
             if (result == FBWebDialogResultDialogNotCompleted) {
                 // User clicked the "x" icon
                 NSLog(@"User canceled request.");
             } else {
                 // Handle the send request callback
                 NSDictionary *urlParams = [self parseURLParams:[resultURL query]];
                 NSLog(@"urlParams--> %@",urlParams);
                 
                 if (![urlParams valueForKey:@"request"]) {
                     // User clicked the Cancel button
                     NSLog(@"User canceled request.");
                 } else {
                     // User clicked the Send button
                     NSString *requestID = [urlParams valueForKey:@"request"];
                     NSLog(@"Request ID: %@", requestID);
                 }
             }
         }
     }];
}

- (NSDictionary*)parseURLParams:(NSString *)query {
    NSArray *pairs = [query componentsSeparatedByString:@"&"];
    NSLog(@"pairs--> %@",pairs);
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    for (NSString *pair in pairs) {
        NSArray *kv = [pair componentsSeparatedByString:@"="];
        NSString *val =
        [kv[1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        params[kv[0]] = val;
    }
    return params;
}

// button to get contacts...
- (IBAction)btnContacts:(id)sender {
    static NSInteger dir = 0;
    ContactsViewController * contact = [[ContactsViewController alloc]initWithNibName:@"ContactsViewController" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    contact.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:contact animated:YES];
}

- (IBAction)btnShoutOutFacebook:(id)sender {
    
    //if([SLComposeViewController isAvailableForServiceType:SLServiceTypeFacebook]) {
        
        SLComposeViewController *controller = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeFacebook];
        [controller setInitialText:@""];
        [self presentViewController:controller animated:YES completion:Nil];
    //}
}

- (IBAction)btnTwitter:(id)sender {
    //if([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter]) {
        
        SLComposeViewController *controller = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
        [controller setInitialText:@""];
        [self presentViewController:controller animated:YES completion:Nil];
   // }
}

- (IBAction)btnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnPromoteQuivu:(id)sender {
    static NSInteger dir = 0;
    PromoteNowScreen * promote = [[PromoteNowScreen alloc]initWithNibName:@"PromoteNowScreen" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    promote.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:promote animated:YES];
}

- (void)request:(FBRequest *)request didLoad:(id)result {
    //ok so it's a dictionary with one element (key="data"), which is an array of dictionaries, each with "name" and "id" keys
   NSArray * items = [(NSDictionary *)result objectForKey:@"data"];
    for (int i=0; i<[items count]; i++) {
        NSDictionary *friend = [items objectAtIndex:i];
        long long fbid = [[friend objectForKey:@"id"]longLongValue];
        NSString *name = [friend objectForKey:@"name"];
        NSLog(@"id: %lld - Name: %@", fbid, name);
    }
}

@end
