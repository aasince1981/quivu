//
//  ChangePasswordViewController.m
//  Reloved
//
//  Created by Kamal on 19/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ChangePasswordViewController.h"

@interface ChangePasswordViewController ()

@end

@implementation ChangePasswordViewController
@synthesize formItems,scrollView,enhancedKeyboard;

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
    
    [tf_CurrentPassword setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_NewPassword setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_NewPasswordAgain setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    tf_CurrentPassword.delegate = self;
    tf_NewPassword.delegate = self;
    tf_NewPasswordAgain.delegate = self;
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_CurrentPassword];
    [self.formItems addObject:tf_NewPassword];
    [self.formItems addObject:tf_NewPasswordAgain];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    scrollView.delegate = self;
    
     lblChangePassword.font = FONT_Lato_Bold(20.0f);
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)BtnSubmit:(id)sender
{
     NSString * UserId = @"";
    if([obNet isObject:KAppDelegate.dictLoginInfo String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        @try {
            UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) { }
    }
    
    NSString * msg = nil;
    if([obNet InternetStatus:YES]){
        if(tf_CurrentPassword.text.length == 0){
            msg = @"Please enter current password!";
        } else if(tf_NewPassword.text.length == 0){
            msg = @"Please enter new password!";
        } else if(tf_NewPassword.text.length < 6){
            msg = @"Length of password atleast 6 characters!";
        } else if (![tf_NewPassword.text isEqualToString:tf_NewPasswordAgain.text]){
            msg = @"Password does not match!";
        }
        
        if(msg) {
            [[[UIAlertView alloc]initWithTitle:@"" message:msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil]show];
        } else {
            NSString *url = [NSString stringWithFormat:@"%@&PreviousPassword=%@&UserNewPassword=%@&UserId=%@",[KAppDelegate getDictServer:WS_ChangePasssword],tf_CurrentPassword.text,tf_NewPassword.text,UserId];
            NSLog(@"ChangePasssword] Url-->%@",url);
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
                NSLog(@"ChangePasssword] Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        [[[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                    } else {
                        [[[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                    }
                }
            }];
        }
    }
}

#pragma mark == textfield delegate methods
- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    [textField resignFirstResponder];
    [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if (textField.frame.origin.y >= KeyboardHeight - 250)
        [self.scrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight + 200) animated:YES];
    [textField setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol
- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        if(tf == tf_NewPasswordAgain){
            [tf_NewPasswordAgain resignFirstResponder];
            [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
        }
        
        if ([tf isEditing] && i!=[self.formItems count]-1) {
            UITextField * tf1 = (UITextField *)[self.formItems objectAtIndex:i+1];
            [tf1 becomeFirstResponder];
            break;
        }
    }
}

- (void)previousDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        
        if(tf == tf_CurrentPassword){
            [tf_CurrentPassword resignFirstResponder];
            [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
        }
        
        if ([tf isEditing] && i!=[self.formItems count]+1 && i != 0)
        {
            UITextField * tf1 = (UITextField *)[self.formItems objectAtIndex:i-1];
            [tf1 becomeFirstResponder];
            break;
        }
    }
}

- (void)doneDidTouchDown
{
    for(UITextField * tf in self.formItems)
    {
        if ([tf isEditing])
        {
            [tf resignFirstResponder];
            [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
            break;
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
