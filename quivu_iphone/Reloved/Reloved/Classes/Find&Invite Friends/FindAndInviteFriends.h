//
//  FindAndInviteFriends.h
//  Reloved
//
//  Created by Kamal on 30/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FindAndInviteFriends : UIViewController <UIScrollViewDelegate>
{
    IBOutlet UILabel *lblHeader;
    IBOutlet UIScrollView *sv_Scrollview;
    IBOutlet UIButton *btnSearchByUserName;
    IBOutlet UIButton *btnRecommendedUsers;
    IBOutlet UIButton *btnPromoteQuivu;
    IBOutlet UILabel *lblFindFriends;
    IBOutlet UIButton *btnFindFriendsFacebook;
    IBOutlet UILabel *lblInviteFriends;
    IBOutlet UIButton *btnInviteFriendsFacebook;
    IBOutlet UIButton *btnContacts;
    IBOutlet UILabel *lblShoutOut;
    IBOutlet UIButton *btnShoutOutFacebook;
    IBOutlet UIButton *btnTwitter;
    IBOutlet UILabel *lblLovingUsingQuivu;
}

- (IBAction)btnSearchByUserName:(id)sender;
- (IBAction)btnFindFriendsFacebook:(id)sender;
- (IBAction)btnInviteFriendsFacebook:(id)sender;
- (IBAction)btnContacts:(id)sender;
- (IBAction)btnShoutOutFacebook:(id)sender;
- (IBAction)btnTwitter:(id)sender;
- (IBAction)btnBack:(id)sender;
- (IBAction)btnPromoteQuivu:(id)sender;
@end
