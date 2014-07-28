//
//  ForgotPassword.h
//  Reloved
//
//  Created by Kamal on 14/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ForgotPassword : UIViewController <UITextFieldDelegate>
{
    IBOutlet UITextField *tf_Email;
}

- (IBAction)BtnSubmit:(id)sender;
- (IBAction)BtnBack:(id)sender;

@end
