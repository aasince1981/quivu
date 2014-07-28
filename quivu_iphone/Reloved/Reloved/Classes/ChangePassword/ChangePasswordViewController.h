//
//  ChangePasswordViewController.h
//  Reloved
//
//  Created by Kamal on 19/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"



@interface ChangePasswordViewController : UIViewController<KSEnhancedKeyboardDelegate,UITextFieldDelegate,UIScrollViewDelegate>
{
    IBOutlet UILabel *lblChangePassword;
    IBOutlet UITextField *tf_CurrentPassword;
    IBOutlet UITextField *tf_NewPassword;
    IBOutlet UITextField *tf_NewPasswordAgain;
}

@property (strong, nonatomic) NSMutableArray *formItems;
@property (strong,nonatomic) IBOutlet UIScrollView *scrollView;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnSubmit:(id)sender;

@end
