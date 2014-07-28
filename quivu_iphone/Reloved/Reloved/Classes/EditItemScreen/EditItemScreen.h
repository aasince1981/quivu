//
//  EditPhotoScreen.h
//  quivu
//
//  Created by Kamal on 19/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"
#import <AviarySDK/AviarySDK.h>

@interface EditItemScreen : UIViewController <UITextFieldDelegate,KSEnhancedKeyboardDelegate, UIPickerViewDataSource,UIPickerViewDelegate, UIAlertViewDelegate,AFPhotoEditorControllerDelegate,UITextViewDelegate>
{
    IBOutlet UILabel *lblSell;
    IBOutlet UILabel *lblCoverPhoto;
    IBOutlet UILabel *lblDetails;
    IBOutlet UILabel *lblCategory;
    IBOutlet UITextField *tf_CateGoryName;
    IBOutlet UILabel *lblItem;
    IBOutlet UITextField *tf_SellingItemName;
    IBOutlet UILabel *lblPrice;
    IBOutlet UITextField *tf_Price;
    IBOutlet UITextView *tv_Message;
    
    IBOutlet UIButton *BtnCategory;
    IBOutlet UIPickerView *picker_mypicker;
    IBOutlet UIView *view_Picker;
    NSMutableArray *arrPickerData;
    NSMutableArray * arrCategoryListing;
    BOOL boolCategory;
    int CategoryId;
    UIButton *btnComman;
    
    IBOutlet UIView *view_MyView;
    IBOutlet UITextField *tf_ItemName;
    IBOutlet UIButton *btnNearByPlace;
 
    IBOutlet UIImageView *imgProduct1;
    IBOutlet UIImageView *imgProduct2;
    IBOutlet UIImageView *imgProduct3;
    IBOutlet UIImageView *imgProduct4;
    IBOutlet UIButton *btnProduct1;
    IBOutlet UIButton *btnProduct2;
    IBOutlet UIButton *btnProduct3;
    IBOutlet UIButton *btnProduct4;
    
    IBOutlet UILabel *lbl_EditPhoto;
    IBOutlet UIImageView *img_EditPhoto;
    IBOutlet UIView * view_EditPhoto;
    
    IBOutlet UIView *View_Delete;
    IBOutlet UIView *view_MarkAsSold;
    IBOutlet UIButton *btnCamera;
    IBOutlet UIButton *btnEdit;
    IBOutlet UIButton *btnDelete;
    IBOutlet UIImageView *imgDelete;
    IBOutlet UIImageView *imgEdit;
}

@property (strong, nonatomic) IBOutlet UIScrollView * sv_ScrollView;
@property (strong, nonatomic) NSMutableArray *formItems;
@property (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;
@property (strong,nonatomic) NSMutableDictionary * dictProductInfo;
@property (strong,nonatomic) NSMutableArray * arrImages;
@property (strong,nonatomic) UIImage *imgProduct;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnRight:(id)sender;

- (IBAction)btn_PickerCancel:(id)sender;
- (IBAction)btn_PickerDone:(id)sender;
- (IBAction)btn_openPicker:(id)sender;

- (IBAction)BtnNearByPlace:(id)sender;
- (IBAction)BtnCross:(id)sender;
- (IBAction)BtnRightView:(id)sender;
- (IBAction)BtnRemoveView:(id)sender;
- (IBAction)btnItem:(id)sender;

- (IBAction)BtnAddProductImage:(id)sender;

- (IBAction)BtnBackView:(id)sender;
- (IBAction)btnCamera:(id)sender;
- (IBAction)BtnEdit:(id)sender;
- (IBAction)BtnDelete:(id)sender;
- (IBAction)MerkAsSold:(id)sender;
- (IBAction)DeleteProduct:(id)sender;

- (IBAction)BtnYesDelete:(id)sender;
- (IBAction)BtnNoDelete:(id)sender;

- (IBAction)BtnYesSold:(id)sender;
- (IBAction)BtnNoSold:(id)sender;
- (IBAction)btnPriceReturnKeyboard:(id)sender;
@end
