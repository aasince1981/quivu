//
//  ViewChatViewController.h
//  Reloved
//
//  Created by Kamal on 27/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"

@interface ViewChatViewController : UIViewController <UIScrollViewDelegate,UITextFieldDelegate,UITableViewDataSource,UITableViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,UIActionSheetDelegate,KSEnhancedKeyboardDelegate>
{
    IBOutlet UILabel *lblViewChat;
    IBOutlet UIScrollView * sv_ScrollView;
    IBOutlet UILabel *lblProductName;
    IBOutlet UILabel *lblProductPrice;
    IBOutlet UIImageView *imgProduct;
    IBOutlet UILabel *lblPriceOfferedText;
    IBOutlet UILabel *lblPriceOffered;
    IBOutlet UILabel *lblDealLocation;
    IBOutlet UILabel *lblDealLocationName;
    IBOutlet UIButton *BtnCancelOffer;
    IBOutlet UILabel *lblChatPrivately;
    IBOutlet UIImageView *imgProductUserImage;
    
    IBOutlet UILabel *lblProductUserName;
    IBOutlet UILabel *lblProductOfferPrice;
    IBOutlet UILabel *lblTimeDuration;
    IBOutlet UITextField *tf_Message;
    
    NSMutableArray * arrCommentList;
    IBOutlet UITableView *tbl_CommentList;
    NSMutableDictionary * DictProductDetails;
    
    IBOutlet UIView *view_PopUp;
    IBOutlet UILabel *lblAlertText;
    IBOutlet UILabel *lblCancelOfferPopUp;
    IBOutlet UIButton *BtnNo;
    IBOutlet UIButton *BtnYes;
    
    UIImagePickerController *imgPkr;
    UIPopoverController *popoverController, *popover;
    
    NSString * strImgMessage;
    NSString  * MessageType;
    UIImage *myImage;
    IBOutlet UIView * view_AddMessage;
}

@property (strong, nonatomic) NSMutableArray *formItems;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

@property (strong, nonatomic) NSMutableDictionary * dictOfferDetails;
@property (strong, nonatomic) NSString * ProductId;

@property BOOL boolCancelOffer;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnReferesh:(id)sender;
- (IBAction)BtnCancelOffer:(id)sender;
- (IBAction)BtnYes:(id)sender;
- (IBAction)BtnNo:(id)sender;
- (IBAction)BtnPlusIcon:(id)sender;
- (IBAction)BtnAddMessage:(id)sender;
@end
