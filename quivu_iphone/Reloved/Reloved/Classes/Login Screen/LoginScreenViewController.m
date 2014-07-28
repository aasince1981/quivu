//
//  LoginScreenViewController.m
//  Reloved
//
//  Created by Kamal on 13/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "LoginScreenViewController.h"
#import "IQValidator.h"
#import "ForgotPassword.h"

@interface LoginScreenViewController ()

@end

@implementation LoginScreenViewController
@synthesize scrollView,formItems,enhancedKeyboard;

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
    [tf_Password setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    tf_Email.delegate = self;
    tf_Password.delegate = self;
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Email];
    [self.formItems addObject:tf_Password];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    scrollView.delegate = self;
    
    BtnLogin.titleLabel.font = FONT_Lato_Bold(17.0f);
    BtnForgotPassword.titleLabel.font = FONT_Lato_Bold(13.0f);
    lblRememberMe.font = FONT_Lato_Light(18.0f);
}

- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_Email.placeholder drawInRect:rect withFont:FONT_Lato_Bold(17.0f)];
    [tf_Password.placeholder drawInRect:rect withFont:FONT_Lato_Bold(17.0f)];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void) viewWillAppear:(BOOL)animated
{
    KAppDelegate.MyVC = self;
    // here we set Remember login info..........
    defaults = [NSUserDefaults standardUserDefaults];
    
    if ([defaults objectForKey:@"IsLoginedIn"] == nil) {
        boolRememberMe = NO;
        [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"blank.png"] forState:UIControlStateNormal];
    } else {
        if ([[defaults objectForKey:@"IsLoginedIn"] length] > 0) {
            boolRememberMe = YES;
            if ([[defaults objectForKey:@"IsLoginedIn"] isEqualToString:@"1"]) {
                [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"check.png"] forState:UIControlStateNormal];
                tf_Email.text = [defaults objectForKey:@"tf_email"];
                tf_Password.text = [defaults objectForKey:@"tf_pass"];
            } else {
                [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"blank.png"] forState:UIControlStateNormal];
            }
        } else {
            boolRememberMe = NO;
            [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"blank.png"] forState:UIControlStateNormal];
        }
    }
}

#pragma mark - Button actions

- (IBAction)BtnRememberMe:(id)sender {
    if(boolRememberMe) {
        [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"blank.png"] forState:UIControlStateNormal];
    } else {
        [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"check.png"] forState:UIControlStateNormal];
    }
    boolRememberMe = !boolRememberMe;
}

- (IBAction)BtnLogin:(id)sender
{
     NSString * msg = nil;
    if([obNet InternetStatus:YES]){
        if(tf_Email.text.length == 0){
            msg = @"Please enter email or Username Address!";
        } else if(tf_Password.text.length == 0){
            msg = @"Please enter password!";
        } else if(tf_Password.text.length < 6){
            msg = @"Length of password atleast 6 characters!";
        }
    
        if(msg) {
            [[[UIAlertView alloc]initWithTitle:@"" message:msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil]show];
        } else {
    
            defaults = [NSUserDefaults standardUserDefaults];
    
            if (boolRememberMe) {
                [defaults setObject:@"1" forKey:@"IsLoginedIn"];
                [defaults setObject:tf_Email.text forKey:@"tf_email"];
                [defaults setObject:tf_Password.text forKey:@"tf_pass"];
            } else {
                [defaults setObject:@"0" forKey:@"IsLoginedIn"];
                [defaults setObject:tf_Email.text forKey:@""];
                [defaults setObject:tf_Password.text forKey:@""];
            }
            [defaults synchronize];
    
            NSString *url = [NSString stringWithFormat:@"%@&UserName=%@&UserPassword=%@&UserNotificationId=%@&UserDeviceId=%@&UserAppVersion=%@&UserDeviceType=%@",[KAppDelegate getDictServer:WS_Login],tf_Email.text,tf_Password.text,KAppDelegate.apnsToken,Device_IMEI,AppVersion,@"2"];
            NSLog(@"Login Url--> %@",url);
            
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:YES WithBlock:^(id json){
                  NSLog(@"Login json--> %@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        KAppDelegate.dictLoginInfo = [json objectForKey:@"UserInformation"];
                        
                        @try {
                            [obNet setDefaultUserData:KAppDelegate.dictLoginInfo WithKey:Key_LoginDetail];
                        } @catch (NSException *exception) {}
                        
                        [KAppDelegate TabBarShow];
    
                    } else {
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

- (IBAction)BtnForgotPassword:(id)sender {
    static NSInteger dir = 0;
    ForgotPassword * forgot = [[ForgotPassword alloc]initWithNibName:@"ForgotPassword" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    forgot.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:forgot animated:YES];
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
    if (textField.frame.origin.y >= KeyboardHeight - 280)
        [self.scrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight + 215) animated:YES];
    [textField setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol
- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        if(tf == tf_Password){
            [tf_Password resignFirstResponder];
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
        
        if(tf == tf_Email){
            [tf_Email resignFirstResponder];
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

@end





