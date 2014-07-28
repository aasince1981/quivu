//
//  EditProfile.m
//  Reloved
//
//  Created by Kamal on 23/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "EditProfile.h"
#import "IQValidator.h"

@interface EditProfile ()

@end

@implementation EditProfile
@synthesize formItems,enhancedKeyboard,EmailVerificationStatus;

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
    
    [tf_UserName setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_Email setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_FirstName setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_LastName setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_EnterMyCity setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_EnterBio setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    tf_UserName.delegate = self;
    tf_Email.delegate = self;
    tf_FirstName.delegate = self;
    tf_LastName.delegate = self;
    tf_EnterMyCity.delegate = self;
    tf_EnterBio.delegate = self;
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_UserName];
    [self.formItems addObject:tf_Email];
    [self.formItems addObject:tf_FirstName];
    [self.formItems addObject:tf_LastName];
    [self.formItems addObject:tf_EnterMyCity];
    [self.formItems addObject:tf_EnterBio];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    sv_ScrollView.delegate = self;
    [self SetFontType];
    [self SetValueToTextField];
    boolProfile = NO;
}

// here we set textfield font and size......
- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_UserName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_Email.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_FirstName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_LastName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_EnterMyCity.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_EnterBio.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
}

// here we set font and size......
- (void) SetFontType {
    tf_Email.font = FONT_Lato_Bold(14.0f);
    tf_UserName.font = FONT_Lato_Bold(14.0f);
    tf_FirstName.font = FONT_Lato_Bold(14.0f);
    tf_LastName.font = FONT_Lato_Bold(14.0f);
    tf_EnterMyCity.font = FONT_Lato_Bold(14.0f);
    tf_EnterBio.font = FONT_Lato_Bold(14.0f);
    tv_Website.font = FONT_Lato_Bold(14.0f);
    lbl_UserName.font = FONT_Lato_Bold(16.0f);
    lblEmail.font = FONT_Lato_Bold(16.0f);
    lbl_FirstName.font = FONT_Lato_Bold(16.0f);
    lbl_LastName.font = FONT_Lato_Bold(16.0f);
    lbl_MyCity.font = FONT_Lato_Bold(16.0f);
    lblWebsite.font = FONT_Lato_Bold(16.0f);
    lbl_Bio.font = FONT_Lato_Bold(16.0f);
    lbl_ProfilePicture.font = FONT_Lato_Bold(16.0f);
    
    btnEmailVerification.titleLabel.font = FONT_Lato_Bold(13.0f);
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    NSLog(@"dictLoginInfo--> %@",KAppDelegate.dictLoginInfo);
}

// set userinfo to view........
- (void) SetValueToTextField {
    if(KAppDelegate.dictLoginInfo.count > 0) {
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            tf_UserName.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserFirstName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            tf_FirstName.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserFirstName"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserLastName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            tf_LastName.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserLastName"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserDefaultCity"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            tf_EnterMyCity.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserDefaultCity"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserBio"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            tf_EnterBio.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserBio"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserBio"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            tf_EnterBio.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserBio"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserWebsiteUrl"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]){
            tv_Website.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserWebsiteUrl"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserEmailAddress"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]){
            tf_Email.text = [KAppDelegate.dictLoginInfo objectForKey:@"UserEmailAddress"];
        }
        
        if([obNet isObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserBio"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            NSString * strImageUrl =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[KAppDelegate.dictLoginInfo objectForKey:@"UserImage"]];
            [obNet SetImageToView:BtnProfilePicture fromImageUrl:strImageUrl Option:5];
        }
        
        @try {
            //NSString * EmailStatus = [KAppDelegate.dictLoginInfo objectForKey:@"UserEmailVerificationStatus"];
            if(EmailVerificationStatus.intValue == 1) {
                btnEmailVerification.hidden = YES;
                view_MyView.frame = CGRectMake(view_MyView.frame.origin.x,152, view_MyView.frame.size.width, view_MyView.frame.size.height);
            } else {
                btnEmailVerification.hidden = NO;
                view_MyView.frame = CGRectMake(view_MyView.frame.origin.x,182, view_MyView.frame.size.width, view_MyView.frame.size.height);
            }
        } @catch (NSException *exception) {}
    }
}

- (BOOL) prefersStatusBarHidden {
    return YES;
}

#pragma mark - Button Actions
// here we select profile image from gellery......
- (IBAction)BtnProfilePicture:(id)sender {
    boolProfile = YES;
    imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    imagePickerController.delegate = self;
    imagePickerController.allowsEditing = YES;
    [self presentModalViewController:imagePickerController animated:YES];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    UIImage *myImage = info[UIImagePickerControllerEditedImage];
    if (myImage) {
        [BtnProfilePicture setImage:myImage forState:UIControlStateNormal];
    }
    
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
}

// edit profile webservice call here........
- (IBAction)BtnRight:(id)sender {

    NSString * userId = @"";
    if(KAppDelegate.dictLoginInfo.count > 0) {
        @try {
            userId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) {}
    }
    
    NSString * msg = nil;
    if([obNet InternetStatus:YES]) {
        if(tf_UserName.text.length == 0){
            msg = @"Please enter user name!";
        } else if(tf_Email.text.length == 0){
            msg = @"Please enter email!";
        } else if (![IQValidator validateEmail:tf_Email.text]) {
            msg = @"Please enter valid Email id!";
        }else if(tf_FirstName.text.length == 0){
            msg = @"Please enter first name!";
        } else if(tf_LastName.text.length == 0){
            msg = @"Please enter last name!";
        } else if(tf_EnterMyCity.text.length == 0){
             msg = @"Please enter city name!";
        } 
        
        if(msg) {
            [[[UIAlertView alloc]initWithTitle:@"" message:msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil]show];
        } else {
            
            NSString * webservice;
                webservice = [NSString stringWithFormat:@"%@&UserName=%@&UserFirstName=%@&UserLastName=%@&UserDefaultCity=%@&UserBio=%@&UserEmailAddress=%@&UserMobileNumber=%@&UserDateofBirth=%@&UserId=%@&UserGender=%@",[KAppDelegate getDictServer:WS_EditProfile],tf_UserName.text,tf_FirstName.text,tf_LastName.text,tf_EnterMyCity.text,tf_EnterBio.text,tf_Email.text,@"",@"",userId,@""];
                NSLog(@"EditProfile Url-->%@",webservice);
            
            UIImage * imgProfile1 = BtnProfilePicture.imageView.image;
            NSLog(@"imgProfile1-%@",imgProfile1);
            
            NSData *fileData1 = UIImageJPEGRepresentation(imgProfile1, 0.2);
            NSLog(@"UploadFileWithFileData-%lu-%@", (unsigned long)fileData1.length, webservice);
            
            webservice = [webservice stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            NSLog(@"UploadFileWithFileData-%lu-%@", (unsigned long)fileData1.length, webservice);
            
            NSURL *url = [NSURL URLWithString:webservice];
            NSMutableURLRequest *request = [[NSMutableURLRequest alloc]initWithURL:url] ;
            request.HTTPMethod = @"POST";
            
            NSString *boundary = @"0xKhTmLbOuNdArY";
            NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary, nil];
            [request addValue:contentType forHTTPHeaderField:@"Content-Type"];
            NSMutableData *body = [NSMutableData data];
            
            // here we upload image to server.........
            if(boolProfile == YES) {
                if (fileData1) {
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"UserImage\"; filename=\"%@\"\r\n", @"hhh.jpg"] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:fileData1];
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                }
            }
            [request setHTTPBody:body];
           
            @try {
                NSURLResponse *response;
                NSData *POSTReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
                NSString *theReply = [[NSString alloc] initWithBytes:[POSTReply bytes] length:[POSTReply length] encoding: NSASCIIStringEncoding];
                
                NSLog(@"theReply-%@", theReply);
                
                NSError * e;
                NSMutableDictionary *dictJSON = [NSJSONSerialization JSONObjectWithData: [theReply dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
                NSLog(@"EditProfile Url-->%@",webservice);
                NSLog(@"UploadFileWithFileDataJSON = %@, e-%@", dictJSON, e);
                if (dictJSON != nil) {
                    if ([[dictJSON objectForKey:@"success"]intValue] == 1) {
                        NSDictionary * dict = [dictJSON objectForKey:@"UserInformation"];
                        if(dict.count > 0) {
                            [self UpdateUserInfo:dict];
                        }
                    [self.navigationController popViewControllerAnimated:YES];
                        
                    } else {
                        [[[UIAlertView alloc]initWithTitle:@"" message:[dictJSON objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                    }
                }
            } @catch (NSException *exception) {}
        }
    }
}

- (void) UpdateUserInfo: (NSDictionary *) dict {
    NSString * UserName;
    NSString * UserFirstName;
    NSString * UserLastName;
    NSString * Email;
    NSString * UserBio;
    NSString * UserImage;
    NSString * EmailStatus;
   
    if([obNet isObject:[dict objectForKey:@"UserName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        UserName = [dict objectForKey:@"UserName"];
    }
    
    if([obNet isObject:[dict objectForKey:@"UserFirstName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        UserFirstName = [dict objectForKey:@"UserFirstName"];
    }
    
    if([obNet isObject:[dict objectForKey:@"UserLastName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        UserLastName = [dict objectForKey:@"UserLastName"];
    }
    
    if([obNet isObject:[dict objectForKey:@"UserEmailAddress"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        Email = [dict objectForKey:@"UserEmailAddress"];
    }
    
    if([obNet isObject:[dict objectForKey:@"UserBio"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        UserBio = [dict objectForKey:@"UserBio"];
    }
    
    if([obNet isObject:[dict objectForKey:@"UserImage"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
        UserImage = [dict objectForKey:@"UserImage"];
    }
    
    @try {
        EmailStatus = [dict objectForKey:@"UserEmailVerificationStatus"];
    } @catch (NSException *exception) {}
    
        UserImage = [dict objectForKey:@"UserImage"];
    
    NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
    dd = [KAppDelegate.dictLoginInfo mutableCopy];
    
    if(UserName.length > 0) {
        @try {
            [dd setObject:UserName forKey:@"UserName"];
        } @catch (NSException *exception) { }
    }
    
    if(UserFirstName.length > 0) {
        @try {
            [dd setObject:UserFirstName forKey:@"UserFirstName"];
        } @catch (NSException *exception) { }
    }
    
    if(UserLastName.length > 0) {
        @try {
            [dd setObject:UserLastName forKey:@"UserLastName"];
        } @catch (NSException *exception) { }
    }
    
    if(Email.length > 0) {
        @try {
            [dd setObject:Email forKey:@"UserEmailAddress"];
        } @catch (NSException *exception) { }
    }
    
    if(UserBio.length > 0) {
        @try {
            [dd setObject:UserBio forKey:@"UserBio"];
        } @catch (NSException *exception) { }
    }
    
    if(UserImage.length > 0) {
        @try {
            [dd setObject:UserImage forKey:@"UserImage"];
        } @catch (NSException *exception) { }
    }
    
    if(EmailStatus.length > 0) {
        @try {
            [dd setObject:EmailStatus forKey:@"UserEmailVerificationStatus"];
        } @catch (NSException *exception) { }
    }
    
    if(dd.count > 0) {
        @try {
            KAppDelegate.dictLoginInfo = dd;
        } @catch (NSException *exception) { }
    }
    
    @try {
        NSMutableDictionary * dict1 = [obNet getDefaultUserDataWithKey:Key_LoginDetail];
        dict1 = [KAppDelegate.dictLoginInfo mutableCopy];
        [obNet setDefaultUserData:dict1 WithKey:Key_LoginDetail];
    } @catch (NSException *exception) {}
}

#pragma mark - Button Actions
- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// button call to send verifgication email....
- (IBAction)btnEmailVerification:(id)sender {
    NSString * UserId;
    @try {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if([obNet InternetStatus:YES]){
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&UserName=%@&UserEmail=%@",[KAppDelegate getDictServer:WS_ResendMail],UserId,tf_UserName.text,tf_Email.text];
        NSLog(@"ResendMail] Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"ResendMail] Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    btnEmailVerification.hidden = YES;
                    view_MyView.frame = CGRectMake(view_MyView.frame.origin.x,152, view_MyView.frame.size.width, view_MyView.frame.size.height);
                } else {
                    [[[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                }
            }
        }];
    }
}

#pragma mark == textfield delegate methods
- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    [textField resignFirstResponder];
    [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if (textField.frame.origin.y >= KeyboardHeight)
        [sv_ScrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight) animated:YES];
    [textField setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol
- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        if(tf == tf_EnterBio){
            [tf_EnterBio resignFirstResponder];
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
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
        
        if(tf == tf_UserName){
            [tf_UserName resignFirstResponder];
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
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
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
            break;
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
