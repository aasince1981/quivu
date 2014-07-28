//
//  ForgotPassword.m
//  Reloved
//
//  Created by Kamal on 14/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ForgotPassword.h"
#import "IQValidator.h"

@interface ForgotPassword ()

@end

@implementation ForgotPassword

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [tf_Email setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    tf_Email.delegate = self;
}

- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_Email.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
}

- (IBAction)BtnSubmit:(id)sender
{
    NSString * msg = nil;
    if([obNet InternetStatus:YES]){
        if(tf_Email.text.length == 0){
            msg = @"Please enter email.";
        } else if (![IQValidator validateEmail:tf_Email.text]) {
            msg = @"Please enter valid email.";
        }
        
        if(msg) {
            [[[UIAlertView alloc]initWithTitle:@"" message:msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil]show];
        } else {
            NSString *url = [NSString stringWithFormat:@"%@&EmailAddress=%@",[KAppDelegate getDictServer:WS_ForgotPassword],tf_Email.text];
            NSLog(@"ForgotPassword Url-->%@",url);
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
                NSLog(@"ForgotPassword Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        self.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
                       [[[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                    } else {
                        self.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
                        [[[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                    }
                }
            }];
        }
    }
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark == textfield delegate methods
- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    [textField resignFirstResponder];
    
    if (textField == tf_Email) {
        [UIView animateWithDuration:0.5 animations:^{
            self.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
        }];
    }
    
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
     if (textField == tf_Email) {
         [UIView animateWithDuration:0.5 animations:^{
             self.view.frame = CGRectMake(0, -180, self.view.frame.size.width, self.view.frame.size.height);
         }];
     }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
