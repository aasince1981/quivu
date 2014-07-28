//
//  LoginScreenViewController.h
//  Reloved
//
//  Created by Kamal on 13/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"

@interface LoginScreenViewController : UIViewController <KSEnhancedKeyboardDelegate,UITextFieldDelegate,UIScrollViewDelegate>
{
    IBOutlet UITextField *tf_Email;
    IBOutlet UITextField *tf_Password;
    IBOutlet UIButton * btnCheckBox;
    
    IBOutlet UIButton * BtnLogin;
    IBOutlet UIButton * BtnForgotPassword;
    IBOutlet UILabel * lblRememberMe;
    
    NSUserDefaults * defaults;
    BOOL boolRememberMe;
}

@property (strong, nonatomic) NSMutableArray *formItems;
@property (strong,nonatomic) IBOutlet UIScrollView *scrollView;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnRememberMe:(id)sender;
- (IBAction)BtnLogin:(id)sender;
- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnForgotPassword:(id)sender;
@end
