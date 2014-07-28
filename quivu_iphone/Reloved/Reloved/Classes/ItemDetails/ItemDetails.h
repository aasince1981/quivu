//
//  ItemDetails.h
//  Reloved
//
//  Created by Kamal on 21/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ItemDetails : UIViewController <UIScrollViewDelegate,UITextFieldDelegate,UITableViewDataSource,UITableViewDelegate>
{
    IBOutlet UILabel *lblItemDetails;
    IBOutlet UIScrollView *scrollView;
    IBOutlet UIScrollView *scrollViewFlipper;
    IBOutlet UIPageControl * pageControl;
    
    IBOutlet UIButton *btnUserTwo;
    IBOutlet UIImageView *imgProductUserImage;
    IBOutlet UILabel *lblUserName;
    IBOutlet UILabel *lblDuration;
    IBOutlet UILabel *lblProductName;
    IBOutlet UILabel *lblPriceOffered;
    IBOutlet UILabel *lblPrice;
    IBOutlet UILabel *lblLikeCount;
    IBOutlet UIButton *btnChatToBuy;
    IBOutlet UIImageView *imgLikeBordered;
    
    IBOutlet UIView *viewOffered;
    IBOutlet UILabel *lblYourOffer;
    IBOutlet UIButton *BtnCancel;
    IBOutlet UIButton *BtnDone;
    IBOutlet UILabel *lblTapToChange;
    IBOutlet UILabel *lblUserNameSellingOffer;
    IBOutlet UILabel *lblMakeAnOffer;
    
    IBOutlet UIButton *BtnLike;
    
    IBOutlet UITextField *tf_OfferPrice;
    NSString * strImageShareUrl;
    IBOutlet UILabel *lblDollerIcon;
    
    NSMutableArray * arrCommentList;
    IBOutlet UITableView *tbl_CommentList;
    
    IBOutlet UIButton *btnReferesh;
    IBOutlet UIButton *btnEdit;
    
    IBOutlet UIView *View_BigImage;
    IBOutlet UIScrollView *sv_ScrollviewBigImage;
    
    
}

@property BOOL flageBtnBackNavigation;

@property (strong, nonatomic) NSString * ProductId;
@property (strong, nonatomic) NSString * CategoryName;
@property (strong, nonatomic) NSString * ProductCatId;

- (IBAction)BtnLike:(id)sender;
- (IBAction)BtnShare:(id)sender;
- (IBAction)BtnComment:(id)sender;

- (IBAction)BtnChatToBuy:(id)sender;
- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnDone:(id)sender;
- (IBAction)BtnCancel:(id)sender;
- (IBAction)BtnReferesh:(id)sender;
- (IBAction)BtnUserTwo:(id)sender;
- (IBAction)BtnItemLikesUser:(id)sender;
- (IBAction)btnCross:(id)sender;
- (IBAction)BtnEdit:(id)sender;

@end
