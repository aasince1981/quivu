//
//  MeViewController.h
//  Reloved
//
//  Created by Kamal on 16/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MeViewController : UIViewController <UIScrollViewDelegate>
{
    IBOutlet UIScrollView * scrollView;
    IBOutlet UIButton *BtnFollowers;
    IBOutlet UIButton *BtnFollowing;
    IBOutlet UIButton *BtnOffersMade;
    IBOutlet UIButton *BtnItemsLiked;
    
    IBOutlet UIImageView *imgUserImage;
    IBOutlet UILabel *lblUserName;
    IBOutlet UILabel *lblCityName;
    IBOutlet UILabel *lblPositiveCount;
    IBOutlet UILabel *lblNeutralCount;
    IBOutlet UILabel *lblNegativeCount;
    IBOutlet UILabel *lblJoinDate;
    IBOutlet UILabel *lblVerified;
    IBOutlet UILabel *lblUserBio;
    
    IBOutlet UIView *ViewItems;
    IBOutlet UIImageView *imgBg;
    IBOutlet UIButton *BtnItem;
    IBOutlet UIImageView *imgLikeIcon;
    IBOutlet UIImageView *imgShareIcon;
    IBOutlet UIImageView *imgGreenDivider;
    IBOutlet UILabel *lblLikeCountValue;
    IBOutlet UIButton *BtnLike;
    IBOutlet UIButton *BtnShare;
    IBOutlet UILabel *lblProductName;
    IBOutlet UIImageView *imgDividerLine;
    IBOutlet UILabel *lblPrice;
    IBOutlet UIImageView *imgSold;
    
    NSMutableArray * arrItemListing;
    NSString * strImageShareUrl;
    IBOutlet UIView *viewFirstInSC;
    IBOutlet UIButton *btnUserWebsiteUrl;
    IBOutlet UIView *view_label;
    IBOutlet UILabel *lblCategoryListingMessage;
    
    IBOutlet UIView *view_PopUp;
    IBOutlet UILabel *lblMessage;
}

- (IBAction)BtnSetting:(id)sender;
- (IBAction)BtnFollower:(id)sender;
- (IBAction)BtnFollowing:(id)sender;
- (IBAction)BtnEditProfile:(id)sender;
- (IBAction)BtnItemsLiked:(id)sender;
- (IBAction)BtnOfferMade:(id)sender;
- (IBAction)BtnFeedBack:(id)sender;
- (IBAction)btnPromote:(id)sender;
- (IBAction)btnReferesh:(id)sender;
- (IBAction)btnUserWebsiteUrl:(id)sender;

@end
