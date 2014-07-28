//
//  RegistrationViewController.m
//  Reloved
//
//  Created by Kamal on 10/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "RegistrationViewController.h"
#import "IQValidator.h"

#define ALPHA                   @"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
#define NUMERIC                 @"1234567890"
#define ALPHA_NUMERIC           ALPHA NUMERIC

@interface RegistrationViewController ()

@end

@implementation RegistrationViewController
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
    [tf_DisplayName setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_Password setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    tf_Email.delegate = self;
    tf_DisplayName.delegate = self;
    tf_Password.delegate = self;
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Email];
    [self.formItems addObject:tf_DisplayName];
    [self.formItems addObject:tf_Password];

    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    scrollView.delegate = self;
     
    DeviceType = @"";
    Type = @"";
    
    lblPassword.font = FONT_Lato_Light(15.0f);
    BtnSignUp.titleLabel.font = FONT_Lato_Bold(15.0f);
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated
{
    KAppDelegate.MyVC = self;
    [self GetCityName];
}

- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_Email.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
    [tf_DisplayName.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
    [tf_Password.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
}

- (void) GetCityName
{
    CLLocationManager * lManager = [AppDelegate getLoactionManager];
    latitude = [NSString stringWithFormat:@"%f", lManager.location.coordinate.latitude];
    longitude = [NSString stringWithFormat:@"%f", lManager.location.coordinate.longitude];
    
   // double latdouble = [latitude doubleValue];
   // double londouble = [longitude doubleValue];
    
    if([obNet InternetStatus:YES]) {
        NSString * url = [NSString stringWithFormat:@"http://maps.google.com/maps/api/geocode/json?latlng=%@,%@&output=json&sensor=false", latitude, longitude];
         NSLog(@"Google Api-%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json){
           // NSLog(@"Google Api json value-->%@", json);
            
            if (json != nil) {
                if ([[json objectForKey:@"status"] isEqualToString:@"OK"]) {
                    if([obNet isObject:[json objectForKey:@"results"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                        NSArray * arr = [json objectForKey:@"results"];
                        @try {
                            NSDictionary * dd = [arr objectAtIndex:4];
                            NSArray * arrr = [dd objectForKey:@"address_components"];
                            CityName = [[arrr objectAtIndex:0] objectForKey:@"long_name"];
                        } @catch (NSException *exception) { }
                    }
                }
            }
        }];
    }
}

#pragma mark - Button actions
- (IBAction)BtnSignUp:(id)sender
{
    NSString * msg = nil;
    if([obNet InternetStatus:YES]){
        if(tf_Email.text.length == 0){
            msg = @"Please enter email.";
        } else if (![IQValidator validateEmail:tf_Email.text]) {
            msg = @"Please enter valid Email id.";
        } else if(tf_DisplayName.text.length == 0){
            msg = @"Please enter username.";
        } else if(tf_DisplayName.text.length < 4){
            msg = @"Length of username atleast 4 characters.";
        } else if(tf_Password.text.length == 0){
            msg = @"Please enter password.";
        } else if(tf_Password.text.length < 6){
            msg = @"Length of password atleast 6 characters.";
        }
        
        if(msg) {
            [[[UIAlertView alloc]initWithTitle:@"" message:msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil]show];
        } else {
            
            DeviceType = @"2";
            Type = @"1";

            NSString *url = [NSString stringWithFormat:@"%@&UserName=%@&UserPassword=%@&UserEmailAddress=%@&UserCity=%@&UserLatitude=%@&UserLongitude=%@&UserNotificationId=%@&UserAppVersion=%@&UserDeviceId=%@&UserDefaultCity=%@&Type=%@&UserDeviceType=%@",[KAppDelegate getDictServer:WS_UserRegistrations],tf_DisplayName.text,tf_Password.text,tf_Email.text,@"",latitude,longitude,KAppDelegate.apnsToken,AppVersion,Device_IMEI,CityName,Type,DeviceType];
            NSLog(@"UserRegistrations Url-->%@",url);
           [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"UserRegistrations Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    KAppDelegate.dictLoginInfo = [json objectForKey:@"UserInformation"];
                    
                    @try {
                        [obNet setDefaultUserData:KAppDelegate.dictLoginInfo WithKey:Key_LoginDetail];
                    } @catch (NSException *exception) {}
                    
                    [KAppDelegate TabBarShow];
                } else {
                    [[[UIAlertView alloc] initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                }
            }
           }];
        }
    }
}

- (IBAction)BtnShowPassword:(id)sender {
    if(boolShowPassword) {
        [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"blank.png"] forState:UIControlStateNormal];
        tf_Password.secureTextEntry = YES;
    } else {
        [btnCheckBox setBackgroundImage:[UIImage imageNamed:@"check.png"] forState:UIControlStateNormal];
        tf_Password.secureTextEntry = NO;
    }
    boolShowPassword = !boolShowPassword;
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
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
        [self.scrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight + 180) animated:YES];
    [textField setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
}

-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSCharacterSet *unacceptedInput = nil;
    
    if (textField == tf_DisplayName) {
        unacceptedInput = [[NSCharacterSet characterSetWithCharactersInString:[ALPHA_NUMERIC stringByAppendingString:@"._"]] invertedSet];
        return ([[string componentsSeparatedByCharactersInSet:unacceptedInput] count] <= 1);
    }
    
    return YES;
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
