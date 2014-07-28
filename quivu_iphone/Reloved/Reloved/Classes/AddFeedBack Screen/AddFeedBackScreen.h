//
//  AddFeedBackScreen.h
//  quivu
//
//  Created by Kamal on 09/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"

@interface AddFeedBackScreen : UIViewController <UIScrollViewDelegate,UITextViewDelegate,KSEnhancedKeyboardDelegate>
{
    IBOutlet UIImageView *imgUserImage;
    IBOutlet UILabel *lblUserName;
    IBOutlet UILabel *lblYourExperience;
    IBOutlet UILabel *lblDescribeExperience;
    IBOutlet UITextView *tv_Experience;
    IBOutlet UILabel *lblCounter;
    
    BOOL boolSeller;
    BOOL boolBuyer;
    
    IBOutlet UIButton * BtnSeller;
    IBOutlet UIButton * BtnBuyer;
    
    IBOutlet UIView *view_Seller;
    IBOutlet UIView *view_Buyer;
    
    BOOL boolPositive;
    BOOL boolNeutral;
    BOOL boolNegative;
    IBOutlet UIButton * BtnPositive;
    IBOutlet UIButton * BtnNeutral;
    IBOutlet UIButton * BtnNegative;
    
    IBOutlet UIView *view_Positive;
    IBOutlet UIView *view_Neutral;
    IBOutlet UIView *view_Negative;
    IBOutlet UIScrollView *sv_scrollview;
}

@property (strong, nonatomic) NSMutableArray * arrFeedbackData;
@property (strong, nonatomic) NSMutableDictionary * dictUserInfo;
@property (strong, nonatomic) NSMutableArray *formItems;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnPositive:(id)sender;
- (IBAction)BtnNeutral:(id)sender;
- (IBAction)BtnNegative:(id)sender;

- (IBAction)BtnSeller:(id)sender;
- (IBAction)BtnBuyer:(id)sender;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnRightIcon:(id)sender;

@end
