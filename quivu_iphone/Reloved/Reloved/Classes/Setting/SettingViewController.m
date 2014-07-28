//
//  SettingViewController.m
//  Reloved
//
//  Created by Kamal on 19/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "SettingViewController.h"
#import "ChangePasswordViewController.h"
#import "WelcomeScreenViewController.h"
#import "NotificationScreen.h"

@interface SettingViewController ()

@end

@implementation SettingViewController

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
}

- (void) SetFontType {
    lblAboutUs.font = FONT_Lato_Bold(20.0f);
    lblSetting.font = FONT_Lato_Bold(20.0f);
    lblApplicationAction.font = FONT_Lato_Bold(17.0f);
    lblMoreInfomation.font = FONT_Lato_Bold(17.0f);
    btnShareSetting.titleLabel.font = FONT_Lato_Light(16.0f);
    btnNotifications.titleLabel.font = FONT_Lato_Light(16.0f);
    btnChangePassword.titleLabel.font = FONT_Lato_Light(16.0f);
    btnLogout.titleLabel.font = FONT_Lato_Light(16.0f);
    btnEmailSupport.titleLabel.font = FONT_Lato_Light(16.0f);
    btnCommunityRules.titleLabel.font = FONT_Lato_Light(16.0f);
    btnAbout.titleLabel.font = FONT_Lato_Light(16.0f);
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated
{
    KAppDelegate.MyVC = self;
}

#pragma mark - Button Actions
- (IBAction)BtnChangePassword:(id)sender {
    static NSInteger dir = 0;
    ChangePasswordViewController * vc = [[ChangePasswordViewController alloc]initWithNibName:@"ChangePasswordViewController" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)BtnLogout:(id)sender {
    
    if (FBSession.activeSession.isOpen)
    {
        [FBSession.activeSession closeAndClearTokenInformation];
        [FBSession.activeSession close];
        [FBSession setActiveSession:nil];
    }
    
    static NSInteger dir = 0;
    WelcomeScreenViewController * vc = [[WelcomeScreenViewController alloc]initWithNibName:@"WelcomeScreenViewController" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    
    NSUserDefaults * defaults  = [NSUserDefaults standardUserDefaults];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:Key_LoginDetail];
    [defaults synchronize];
    
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnEmailSupport:(id)sender {
    //NSString *emailTitle = @"";
   // NSString *messageBody = @"";
    NSArray *toRecipents = [NSArray arrayWithObject:@"support@quivu.com"];
    
    MFMailComposeViewController *mc = [[MFMailComposeViewController alloc] init];
    mc.mailComposeDelegate = self;
    //[mc setSubject:emailTitle];
    //[mc setMessageBody:messageBody isHTML:NO];
    [mc setToRecipients:toRecipents];
    [self presentViewController:mc animated:YES completion:NULL];
}

- (void) mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
    switch (result)
    {
        case MFMailComposeResultCancelled:
            NSLog(@"Mail cancelled");
            break;
        case MFMailComposeResultSaved:
            NSLog(@"Mail saved");
            break;
        case MFMailComposeResultSent:
            NSLog(@"Mail sent");
            break;
        case MFMailComposeResultFailed:
            NSLog(@"Mail sent failure: %@", [error localizedDescription]);
            break;
        default:
           break;
   }
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (IBAction)btnCommunityRules:(id)sender {
    lblAboutUs.text = @"Community Rules";
    [self DownloadDataWithContentType:WS_CommunityRule];
}

- (IBAction)btnAbout:(id)sender {
    lblAboutUs.text = @"About Quivu";
    [self DownloadDataWithContentType:WS_AboutApp];
}

- (IBAction)btnRemoveView:(id)sender {
    [view_Webview removeFromSuperview];
}

- (IBAction)BtnNotification:(id)sender {
    static NSInteger dir = 0;
    NotificationScreen * vc = [[NotificationScreen alloc]initWithNibName:@"NotificationScreen" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - Method for Download Data:
- (void) DownloadDataWithContentType : (NSString *) contentType {
    if ([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&title=%@",[KAppDelegate getDictServer:WS_GetContentPage],contentType];
        NSLog(@"getContentPage Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"getContentPage Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    view_Webview.frame = self.view.frame;
                    [self.view addSubview:view_Webview];
                    NSString * strContent = [json objectForKey:@"Content"];
                    
                    @try {
                        NSData * d = [[NSData alloc] init];
                        d = [NSData dataFromBase64String:strContent];
                        NSString* newStr = [[NSString alloc] initWithData:d encoding:NSUTF8StringEncoding];
                        newStr = [newStr stringByDecodingHTMLEntities];
                        [webView_Content setBackgroundColor:[UIColor clearColor]];
                        [webView_Content setOpaque:NO];
                        [webView_Content loadHTMLString:newStr baseURL:[[NSURL alloc] init]];
                    } @catch (NSException *exception) {}
                }
            }
        }];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
