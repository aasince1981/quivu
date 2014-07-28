//
//  ItemsLiked.h
//  Reloved
//
//  Created by Kamal on 26/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ItemsLiked : UIViewController <UIScrollViewDelegate>
{
    IBOutlet UILabel *lblItemLikes;
    IBOutlet UIScrollView * scrollView;
    IBOutlet UILabel *lblItemsLiked;
    IBOutlet UIView * view_CategoryList;
    IBOutlet UIImageView *imgBg;
    IBOutlet UIButton *btnCategoryView;
    IBOutlet UIImageView *imgHeader;
    IBOutlet UILabel *lblProductName;
    IBOutlet UIImageView *imgBottom;
    IBOutlet UILabel *lblPrice;
    IBOutlet UILabel *lblDividerLine;
    
    
    IBOutlet UIImageView *imgUserImage;
    IBOutlet UILabel *lblProductUserName;
    IBOutlet UILabel *lblDuration;
    IBOutlet UIImageView *imgCategoryImage;
    IBOutlet UIImageView *imgLike;
    IBOutlet UILabel *lblLikeCount;
    IBOutlet UIImageView *imgShare;
    IBOutlet UIImageView *imgDivider;
    IBOutlet UIImageView *imgClock;
    IBOutlet UIButton *BtnLike;
    IBOutlet UIButton *BtnShare;
    
    NSMutableArray * arrItemListing;
    
    NSString * strImageShareUrl;
}
- (IBAction)Btnback:(id)sender;
- (IBAction)BtnReferesh:(id)sender;

@end
