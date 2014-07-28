//
//  EditProfile.h
//  Reloved
//
//  Created by Kamal on 23/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"


@interface EditProfile : UIViewController <UIScrollViewDelegate,UITextFieldDelegate,KSEnhancedKeyboardDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate>
{
    IBOutlet UIScrollView * sv_ScrollView;
    IBOutlet UILabel *lbl_UserName;
    IBOutlet UILabel *lblEmail;
    IBOutlet UILabel *lbl_FirstName;
    IBOutlet UILabel *lbl_LastName;
    IBOutlet UILabel *lbl_MyCity;
    IBOutlet UILabel *lblWebsite;
    IBOutlet UILabel *lbl_Bio;
    IBOutlet UILabel *lbl_ProfilePicture;
    
    IBOutlet UITextField *tf_UserName;
    IBOutlet UITextField *tf_Email;
    IBOutlet UITextField *tf_FirstName;
    IBOutlet UITextField *tf_LastName;
    IBOutlet UITextField *tf_EnterMyCity;
    IBOutlet UITextView *tv_Website;
    IBOutlet UITextField *tf_EnterBio;
    IBOutlet UIButton *BtnProfilePicture;
    IBOutlet UIView *view_MyView;
    
    IBOutlet UIButton *btnEmailVerification;
    UIImagePickerController *imagePickerController;
    
    BOOL boolProfile;
}
@property (strong, nonatomic) NSString * EmailVerificationStatus;
@property (strong, nonatomic) NSMutableArray *formItems;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnProfilePicture:(id)sender;
- (IBAction)BtnRight:(id)sender;
- (IBAction)BtnBack:(id)sender;
- (IBAction)btnEmailVerification:(id)sender;
@end
