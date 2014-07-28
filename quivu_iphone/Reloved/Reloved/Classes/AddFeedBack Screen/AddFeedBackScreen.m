//
//  AddFeedBackScreen.m
//  quivu
//
//  Created by Kamal on 09/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "AddFeedBackScreen.h"

@interface AddFeedBackScreen ()
{
    NSString * LoginUserId;
    NSString * ToUserId;
    NSString * ToUserImage;
    NSString * ToUserName;
    NSString * FeedbackType;
    NSString * FeedbackExperience;
}

@end

@implementation AddFeedBackScreen
@synthesize arrFeedbackData,dictUserInfo,formItems,enhancedKeyboard;

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
    
    tv_Experience.delegate = self;
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tv_Experience];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    sv_scrollview.delegate = self;
    
    boolPositive = YES;
    boolNegative = NO;
    boolNeutral = NO;
    boolSeller = YES;
    boolBuyer = NO;
    
    LoginUserId = @"";
    ToUserId = @"";
    ToUserImage = @"";
    FeedbackType = @"";
    FeedbackExperience = @"1";
    ToUserName = @"";
    
    tv_Experience.delegate = self;
    view_Positive.frame = CGRectMake(0, 220, 320, 45);
    [sv_scrollview addSubview:view_Positive];
    
    [self SetFontType];
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self SetValueUserInfo];
    [tv_Experience setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
}

// set UserInfo to view.......
- (void) SetValueUserInfo {
    
    if(KAppDelegate.dictLoginInfo.count > 0) {
        LoginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    }
    
    if(arrFeedbackData.count > 0) {
        for (int i =0; i < arrFeedbackData.count; i++) {
            NSDictionary * dict = [arrFeedbackData objectAtIndex:i];
            NSString * FeedbackByUserId = [dict objectForKey:@"FeedbackByUserId"];
            
            if(LoginUserId.intValue == FeedbackByUserId.intValue) {
                
                @try {
                    FeedbackExperience = [[arrFeedbackData objectAtIndex:i] objectForKey:@"FeedbackExperience"];
                    FeedbackType = [[arrFeedbackData objectAtIndex:i] objectForKey:@"FeedbackType"];
                    tv_Experience.text = [[arrFeedbackData objectAtIndex:i] objectForKey:@"FeedbackDescription"];
                    
                    NSString *substring = [NSString stringWithString:tv_Experience.text];
                    lblCounter.text = [NSString stringWithFormat:@"%lu", 500-substring.length];
                } @catch (NSException *exception) {}
                
                if(FeedbackExperience.intValue == 1) {
                    view_Positive.frame = CGRectMake(0, 220, 320, 45);
                    [sv_scrollview addSubview:view_Positive];
                    
                } else if (FeedbackExperience.intValue == 2) {
                    view_Neutral.frame = CGRectMake(0, 220, 320, 45);
                    [sv_scrollview addSubview:view_Neutral];
                    
                } else if (FeedbackExperience.intValue == 3) {
                    view_Negative.frame = CGRectMake(0, 220, 320, 45);
                    [sv_scrollview addSubview:view_Negative];
                }
                
                if(FeedbackType.intValue == 1) {
                    view_Seller.frame = CGRectMake(0, 130, 320, 45);
                    [sv_scrollview addSubview:view_Seller];
                    
                } else if (FeedbackType.intValue == 2) {
                    view_Buyer.frame = CGRectMake(0, 130, 320, 45);
                    [sv_scrollview addSubview:view_Buyer];
                }
            }
        }
    }
    
    if(dictUserInfo.count > 0) {
        @try {
            ToUserId = [dictUserInfo objectForKey:@"UserId"];
            ToUserImage = [dictUserInfo objectForKey:@"UserImage"];
            ToUserName = [dictUserInfo objectForKey:@"UserName"];
            lblUserName.text = [dictUserInfo objectForKey:@"UserName"];
            
            NSString * strImageUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dictUserInfo objectForKey:@"UserImage"]];
            [obNet SetImageToView:imgUserImage fromImageUrl:strImageUrl Option:5];
            
        } @catch (NSException *exception) {}
    }
}

// here we set Custom FontType and Size ........
- (void) SetFontType {
    lblUserName.font = FONT_Lato_Bold(17.0f);
    lblCounter.font = FONT_Lato_Bold(17.0f);
    lblDescribeExperience.font = FONT_Lato_Bold(20.0f);
    lblYourExperience.font = FONT_Lato_Bold(20.0f);
    
    BtnSeller.titleLabel.font = FONT_Lato_Bold(14.0f);
    BtnBuyer.titleLabel.font = FONT_Lato_Bold(14.0f);
    BtnPositive.titleLabel.font = FONT_Lato_Bold(14.0f);
    BtnNeutral.titleLabel.font = FONT_Lato_Bold(14.0f);
    BtnNegative.titleLabel.font = FONT_Lato_Bold(14.0f);
}

#pragma mark - Button actions
// Button Seller..........

- (IBAction)BtnSeller:(id)sender {
    FeedbackType = @"1";
    [view_Buyer removeFromSuperview];
    view_Seller.frame = CGRectMake(0, 130, 320, 45);
    [sv_scrollview addSubview:view_Seller];
    
    boolSeller = YES;
    boolBuyer = NO;
}

// Button Buyer..........
- (IBAction)BtnBuyer:(id)sender {
    FeedbackType = @"2";
    [view_Seller removeFromSuperview];
    view_Buyer.frame = CGRectMake(0, 130, 320, 45);
    [sv_scrollview addSubview:view_Buyer];
    
    boolSeller = NO;
    boolBuyer = YES;
}

// Button Positive..........
- (IBAction)BtnPositive:(id)sender {
    FeedbackExperience = @"1";
    [view_Negative removeFromSuperview];
    [view_Neutral removeFromSuperview];
    view_Positive.frame = CGRectMake(0, 220, 320, 45);
    [sv_scrollview addSubview:view_Positive];
    
    boolPositive = YES;
    boolNegative = NO;
    boolNeutral = NO;
}

// Button Neutral..........
- (IBAction)BtnNeutral:(id)sender {
     FeedbackExperience = @"2";
    [view_Negative removeFromSuperview];
    [view_Positive removeFromSuperview];
    view_Neutral.frame = CGRectMake(0, 220, 320, 45);
    [sv_scrollview addSubview:view_Neutral];
    
    boolPositive = NO;
    boolNegative = NO;
    boolNeutral = YES;
}

// Button Negative..........
- (IBAction)BtnNegative:(id)sender {
    FeedbackExperience = @"3";
    [view_Neutral removeFromSuperview];
    [view_Positive removeFromSuperview];
    view_Negative.frame = CGRectMake(0, 220, 320, 45);
    [sv_scrollview addSubview:view_Negative];
    
    boolPositive = NO;
    boolNegative = YES;
    boolNeutral = NO;
}

// Button Back..........
- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// Button Right Icon for add Feedback............
- (IBAction)BtnRightIcon:(id)sender {
    NSString * FromUserName = @"";
    NSString * FromUserImage = @"";
    NSString * FeedbackByUserId = @"";
    NSString * FeedbackId = @"";
    
    NSString * msg = nil;
    if([FeedbackType isEqualToString:@""]) {
        msg = @"Please select seller or buyer";
    } else if ([FeedbackExperience isEqualToString:@""]) {
        msg = @"Please select positive, neutral or negative";
    } else if (tv_Experience.text.length == 0) {
        msg = @"Please describe your experience";
    }
    
    if(msg) {
        [obNet PopUpMSG:msg Header:@""];
    } else {
        if(KAppDelegate.dictLoginInfo.count > 0) {
            @try {
                FromUserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
            } @catch (NSException *exception) {}
            
            @try {
                FromUserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
            } @catch (NSException *exception) {}
        }
        
        if([obNet InternetStatus:YES]) {
            NSString *url;
            
            if(arrFeedbackData.count > 0) {
                for (int i =0; i < arrFeedbackData.count; i++) {
                    NSDictionary * dict = [arrFeedbackData objectAtIndex:i];
                    
                    FeedbackByUserId = [dict objectForKey:@"FeedbackByUserId"];
                    FeedbackId = [dict objectForKey:@"FeedbackId"];
                    
                    if(LoginUserId.intValue == FeedbackByUserId.intValue) {
                        url = [NSString stringWithFormat:@"%@&FeedbackId=%@&FeedbackReplyStatus=%@&FeedbackMessage=%@&FeedbackType=%@&FeedbackExperience=%@",[KAppDelegate getDictServer:WS_EditFeedback],FeedbackId,@"0",tv_Experience.text,FeedbackType,FeedbackExperience];
                        NSLog(@"EditFeedback Url-->%@",url);
                        break;
                    } else {
                        url = [NSString stringWithFormat:@"%@&FeedbackFromUserId=%@&FeedbackFromUserName=%@&FeedbackFromUserImage=%@&FeedbackToUserId=%@&FeedbackReplyStatus=%@&FeedbackDescription=%@&FeedbackType=%@&FeedbackExperience=%@&FeedbackToUserImage=%@&FeedbackToUserName=%@",[KAppDelegate getDictServer:WS_AddFeedback],LoginUserId,FromUserName,FromUserImage,ToUserId,@"0",tv_Experience.text,FeedbackType,FeedbackExperience,ToUserImage,ToUserName];
                        NSLog(@"AddFeedback Url-->%@",url);
                    }
                }
            } else {
                    url = [NSString stringWithFormat:@"%@&FeedbackFromUserId=%@&FeedbackFromUserName=%@&FeedbackFromUserImage=%@&FeedbackToUserId=%@&FeedbackReplyStatus=%@&FeedbackDescription=%@&FeedbackType=%@&FeedbackExperience=%@&FeedbackToUserImage=%@&FeedbackToUserName=%@",[KAppDelegate getDictServer:WS_AddFeedback],LoginUserId,FromUserName,FromUserImage,ToUserId,@"0",tv_Experience.text,FeedbackType,FeedbackExperience,ToUserImage,ToUserName];
                    NSLog(@"AddFeedback Url-->%@",url);
                }
           
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
                NSLog(@"Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        [self.navigationController popViewControllerAnimated:YES];
                    } else {
                        [obNet PopUpMSG:[json objectForKey:@"msg"] Header:@""];
                    }
                }
            }];
        }
    }
}

#pragma mark - textView Delegate methods
- (void) textViewDidBeginEditing:(UITextView *)textView {
    
   if(IS_IPHONE_5) {
       if (textView.frame.origin.y >= KeyboardHeight)
           [sv_scrollview setContentOffset:CGPointMake(0, textView.frame.origin.y - KeyboardHeight - 90) animated:YES];
    } else {
        if (textView.frame.origin.y >= KeyboardHeight)
            [sv_scrollview setContentOffset:CGPointMake(0, textView.frame.origin.y - KeyboardHeight - 30) animated:YES];
    }
    
    if ([textView.text isEqualToString:@"Enter experience"]) {
        tv_Experience.text = @"";
        tv_Experience.textColor = [UIColor darkGrayColor]; //optional
    }
}

- (void)textViewDidEndEditing:(UITextView *)textView
{
    if ([textView.text isEqualToString:@""]) {
        textView.textColor = [UIColor darkGrayColor];
    }
    [sv_scrollview setContentOffset:CGPointMake(0, 0) animated:YES];
    [textView resignFirstResponder];
}

- (void)textViewDidChange:(UITextView *)textView {
    
    NSString *substring = [NSString stringWithString:tv_Experience.text];
    if (substring.length > 0) {
        lblCounter.text = [NSString stringWithFormat:@"%lu", 500-substring.length];
    }
    
    if (substring.length == 500 || substring.length > 500) {
        [textView resignFirstResponder];
    }
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
       [sv_scrollview setContentOffset:CGPointMake(0, 0) animated:YES];
        return NO;
    }
    
    return YES;
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol
- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextView * tv = (UITextView *)[self.formItems objectAtIndex:i];
        if(tv == tv_Experience){
            [tv_Experience resignFirstResponder];
            [sv_scrollview setContentOffset:CGPointMake(0, 0) animated:YES];
        }
    }
}

- (void)previousDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextView * tv = (UITextView *)[self.formItems objectAtIndex:i];
        
        if(tv == tv_Experience){
            [tv_Experience resignFirstResponder];
            [sv_scrollview setContentOffset:CGPointMake(0, 0) animated:YES];
        }
    }
}

- (void)doneDidTouchDown
{
    for(UITextField * tf in self.formItems)
    {
        if ([tf isEditing])
        {
            [tv_Experience resignFirstResponder];
            [sv_scrollview setContentOffset:CGPointMake(0, 0) animated:YES];
            break;
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
