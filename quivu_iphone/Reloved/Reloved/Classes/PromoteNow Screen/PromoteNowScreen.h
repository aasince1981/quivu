//
//  PromoteNowScreen.h
//  quivu
//
//  Created by Kamal on 10/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Social/Social.h>
#import <Accounts/Accounts.h>
#import <Twitter/Twitter.h>

@interface PromoteNowScreen : UIViewController<UIDocumentInteractionControllerDelegate>
{
    UIImage *imgCaptureImage;
    
    IBOutlet UIView *view_ImageBg;
    IBOutlet UIButton *btnPromoteHeader;
    IBOutlet UIButton *btnPromoteBottom;
    IBOutlet UIScrollView *sv_SccrollView;
    IBOutlet UILabel *lblPromote;
    IBOutlet UILabel *lblPromoteQuivu;
    IBOutlet UILabel *lblShoppingQuivu;
    IBOutlet UILabel *lblPromoteOn;
    
    IBOutlet UIImageView *imgUserImage;
    IBOutlet UILabel *lblUserName;
    
    IBOutlet UIImageView *imgDefaults;
    IBOutlet UIView *view_SingleImage;
    IBOutlet UIImageView *imgFirstImage;
    IBOutlet UIView *view_MultipleImage;
    IBOutlet UIImageView *imgFirst;
    IBOutlet UIImageView *imgSecond;
    IBOutlet UIImageView *imgThird;

    NSMutableArray * arrImages;

    IBOutlet UIView * view_Defaults;
    IBOutlet UIView * view_Facebook;
    IBOutlet UIView * view_Twitter;
    IBOutlet UIView * view_Instagram;
    
    BOOL boolFacebook;
    BOOL boolTwitter;
    BOOL boolInstagram;
    
    SLComposeViewController *mySLComposerSheet;
}

@property (nonatomic, strong) ACAccountStore *accountStore;
@property (nonatomic, retain) UIDocumentInteractionController *dic;

- (IBAction)BtnFacebook:(id)sender;
- (IBAction)BtnTwitter:(id)sender;
- (IBAction)BtnInstagram:(id)sender;
- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnPromoteNow:(id)sender;

@end
