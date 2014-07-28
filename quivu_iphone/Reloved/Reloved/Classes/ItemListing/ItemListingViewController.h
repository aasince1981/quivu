//
//  ItemListingViewController.h
//  Reloved
//
//  Created by Kamal on 19/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"
#import "UIImageView+Custom.h"

@interface ItemListingViewController : UIViewController <UIScrollViewDelegate,UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate,KSEnhancedKeyboardDelegate>
{
    IBOutlet UIScrollView * scrollView;
    
    IBOutlet UILabel *lblCategoryName;
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
    IBOutlet UIButton *btnFilterIcon;
    
    IBOutlet UITextField *tf_Search;
    NSMutableArray * arrItemListing;
     NSString * strImageShareUrl;
    
    IBOutlet UIView *view_TapRemoveFilter;
    IBOutlet UIView *view_Filter;
    IBOutlet UIScrollView *sv_Filter;
    IBOutlet UILabel *lblSortBy;
    IBOutlet UILabel *lblPopular;
    IBOutlet UIButton *BtnPopular;
    IBOutlet UILabel *lblSearchWithIn;
    IBOutlet UILabel *lblCategory;
    IBOutlet UIButton *BtnCategory;
    IBOutlet UILabel *lblSliderValue;
    IBOutlet UISlider *slider;
    IBOutlet UITextField *tf_MinimumPrice;
    IBOutlet UITextField *tf_MaximumPrice;
    IBOutlet UILabel *lblPriceRange;
    IBOutlet UIButton *BtnReset;
    
    IBOutlet UILabel *lblfilter;
    
    IBOutlet UIPickerView *picker_mypicker;
    IBOutlet UIView *view_Picker;
    NSMutableArray *arrPickerData;
    NSMutableArray * arrPopularListing;
    NSMutableArray * arrCategoryListing;
    BOOL boolPopular;
    BOOL boolCategory;
    int PopularId;
    int CategoryId;
    UIButton *btnComman;
    
    UISwipeGestureRecognizer *swipeRecognizer;
    UISwipeGestureRecognizer *swipeRecognizer1;

    IBOutlet UIView * view_PopUp;
    IBOutlet UILabel * lblMessage;
}

@property (strong, nonatomic) NSMutableDictionary * DictProductData;
@property (strong, nonatomic) NSMutableArray *formItems;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnSearch:(id)sender;
- (IBAction)BtnFilter:(id)sender;
- (IBAction)BtnRight:(id)sender;

- (IBAction)btn_PickerCancel:(id)sender;
- (IBAction)btn_PickerDone:(id)sender;
- (IBAction)btn_openPicker:(id)sender;
- (IBAction)SliderAction:(id)sender;
- (IBAction)BtnReset:(id)sender;

@end
