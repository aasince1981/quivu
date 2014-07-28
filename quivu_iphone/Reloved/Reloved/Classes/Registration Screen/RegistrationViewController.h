//
//  RegistrationViewController.h
//  Reloved
//
//  Created by Kamal on 10/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "KSEnhancedKeyboard.h"

@interface RegistrationViewController : UIViewController <KSEnhancedKeyboardDelegate,UITextFieldDelegate,UIScrollViewDelegate,CLLocationManagerDelegate>
{
    IBOutlet UITextField *tf_Email;
    IBOutlet UITextField *tf_DisplayName;
    IBOutlet UITextField *tf_Password;

    IBOutlet UILabel * lblPassword;
    IBOutlet UIButton * BtnSignUp;
    IBOutlet UIButton * btnCheckBox;
    NSString * DeviceType;
    NSString * Type;
    NSString *latitude ;
    NSString *longitude;
    NSString *CityName;
    
    BOOL boolShowPassword;
}

@property (strong, nonatomic) NSMutableArray *formItems;
@property (strong,nonatomic) IBOutlet UIScrollView *scrollView;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnSignUp:(id)sender;
- (IBAction)BtnShowPassword:(id)sender;
- (IBAction)BtnBack:(id)sender;

@end
