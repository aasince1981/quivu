//
//  SettingViewController.h
//  Reloved
//
//  Created by Kamal on 19/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>
#import "NSString+HTML.h"
#import "NSData+Base64.h"

@interface SettingViewController : UIViewController <MFMailComposeViewControllerDelegate,UIWebViewDelegate>
{
    IBOutlet UILabel *lblSetting;
    IBOutlet UILabel *lblApplicationAction;
    IBOutlet UILabel *lblMoreInfomation;
    IBOutlet UIButton *btnShareSetting;
    IBOutlet UIButton *btnNotifications;
    IBOutlet UIButton *btnChangePassword;
    IBOutlet UIButton *btnLogout;
    IBOutlet UIButton *btnEmailSupport;
    IBOutlet UIButton *btnCommunityRules;
    IBOutlet UIButton *btnAbout;
    IBOutlet UIWebView *webView_Content;
    IBOutlet UIView *view_Webview;
    IBOutlet UILabel *lblAboutUs;
}
- (IBAction)BtnChangePassword:(id)sender;
- (IBAction)BtnLogout:(id)sender;
- (IBAction)BtnBack:(id)sender;
- (IBAction)btnEmailSupport:(id)sender;
- (IBAction)btnCommunityRules:(id)sender;
- (IBAction)btnAbout:(id)sender;
- (IBAction)btnRemoveView:(id)sender;
- (IBAction)BtnNotification:(id)sender;

@end
