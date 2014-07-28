//
//  ProfileOther.h
//  Reloved
//
//  Created by Kamal on 22/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ProfileOther : UIViewController <UIScrollViewDelegate>
{
    IBOutlet UILabel *lblHeader;
    IBOutlet UIScrollView * scrollView;
    IBOutlet UIImageView *imgUserImage;
    IBOutlet UIButton *BtnFollowers;
    IBOutlet UIButton *BtnFollowing;
    
    IBOutlet UILabel *lblUserBio;
    IBOutlet UILabel *lblUserName;
    IBOutlet UILabel *lblCityName;
    IBOutlet UILabel *lblPositiveCount;
    IBOutlet UILabel *lblNeutralCount;
    IBOutlet UILabel *lblNegativeCount;
    IBOutlet UILabel *lblJoinDate;
    IBOutlet UILabel *lblVerified;
    IBOutlet UIButton *BtnFollow;
    
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
    IBOutlet UILabel *lblPrice;
    IBOutlet UILabel *lblDividerLine;
    IBOutlet UIImageView *imgSold;
    
    NSMutableArray * arrItemListing;
    NSString * strImageShareUrl;
    
    IBOutlet UIView *viewFirstInSC;
    IBOutlet UIButton *btnUserWebsiteUrl;
}

@property (strong, nonatomic) NSString * ProductUserId;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnFollowing:(id)sender;
- (IBAction)BtnFollowers:(id)sender;
- (IBAction)BtnAddFollow:(id)sender;
- (IBAction)BtnFeedBack:(id)sender;
- (IBAction)btnReferesh:(id)sender;
- (IBAction)btnUserWebsiteUrl:(id)sender;

@end


