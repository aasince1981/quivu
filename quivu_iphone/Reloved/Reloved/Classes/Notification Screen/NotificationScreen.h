//
//  NotificationScreen.h
//  Reloved
//
//  Created by Kamal on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NotificationScreen : UIViewController <UIScrollViewDelegate>
{
    IBOutlet UILabel *lblHeader;
    IBOutlet UIScrollView *sv_ScrollView;
    IBOutlet UIImageView *imgVibrateCheckBox;
    IBOutlet UIImageView *imgLightCheckBox;
    IBOutlet UIImageView *imgSoundCheckBox;
    IBOutlet UIImageView *imgItemListedCheckBox;
    IBOutlet UIImageView *imgNewOfferCheckBox;
    IBOutlet UIImageView *imgNewChatCheckBox;
    IBOutlet UIImageView *imgNewCommentCheckBox;
    IBOutlet UIImageView *imgNewOfferPushNotiCheckBox;
    IBOutlet UIImageView *imgNewChatPushNotiCheckBox;
    
    IBOutlet UIButton *btnVibrate;
    IBOutlet UIButton *btnLight;
    IBOutlet UIButton *btnSound;
    IBOutlet UIButton *btnItemListed;
    IBOutlet UIButton *btnNewOffer;
    IBOutlet UIButton *btnNewChat;
    IBOutlet UIButton *btnNewComment;
    IBOutlet UIButton *btnNewOfferPushNoti;
    IBOutlet UIButton *btnNewChatPushNoti;
    
    BOOL boolVibrate;
    BOOL boolLight;
    BOOL boolSound;
    BOOL boolItemListed;
    BOOL boolNewOffer;
    BOOL boolNewChat;
    BOOL boolNewComment;
    BOOL boolNewOfferPushNoti;
    BOOL boolNewChatPushNoti;
}

- (IBAction)btnVibrate:(id)sender;
- (IBAction)btnLight:(id)sender;
- (IBAction)btnSound:(id)sender;
- (IBAction)btnItemListed:(id)sender;
- (IBAction)btnNewOffer:(id)sender;
- (IBAction)btnNewChat:(id)sender;
- (IBAction)btnNewComment:(id)sender;
- (IBAction)btnNewOfferPushNoti:(id)sender;
- (IBAction)btnNewChatPushNoti:(id)sender;
- (IBAction)btnBack:(id)sender;

@end
