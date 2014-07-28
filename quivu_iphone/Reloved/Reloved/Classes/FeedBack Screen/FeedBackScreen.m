//
//  FeedBackScreen.m
//  quivu
//
//  Created by Kamal on 04/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FeedBackScreen.h"
#import "AddFeedBackScreen.h"

@interface FeedBackScreen ()
{
    NSString * LoginUserId;
    NSString * ToUserId;
    NSString * FeedBackId;
    NSString * FeedbackExperience;
    NSString * ReplyStatus;
    NSString * FeedbackType;
    NSString * FeedbackByUserId;
}

@end

@implementation FeedBackScreen
@synthesize dictUserInfo;

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
    
    arrAllFeedBackData = [[NSMutableArray alloc]init];
    arrAsBuyer = [[NSMutableArray alloc]init];
    arrAsSeller = [[NSMutableArray alloc]init];
    
    tbl_feedBack.dataSource = self;
    tbl_feedBack.delegate = self;
    
    boolAll = YES;
    boolAsSeller = NO;
    boolAsBuyer = NO;
    
    view_All.frame = CGRectMake(0, 50, 320, 45);
    [self.view addSubview:view_All];

    tv_FeedbackReply.delegate = self;
    tv_FeedbackEdit.delegate = self;
    
    LoginUserId = @"";
    ToUserId = @"";
    FeedBackId = @"";
    FeedbackByUserId = @"";
    
    FeedbackExperience = @"";
    ReplyStatus = @"";
    FeedbackType = @"";
    
    //NSLog(@"dictUserInfo--> %@",dictUserInfo);
    
    if(dictUserInfo.count > 0) {
        @try {
            ToUserId = [dictUserInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) {}
    }
}

#pragma mark - viewWillAppear method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    
    if(KAppDelegate.dictLoginInfo.count > 0) {
        LoginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    }
    
    if(LoginUserId.intValue == ToUserId.intValue) {
        btnPlusIcon.hidden = YES;
        BtnReferesh.frame = CGRectMake(btnPlusIcon.frame.origin.x, BtnReferesh.frame.origin.y, BtnReferesh.frame.size.width, BtnReferesh.frame.size.height);
        [self.view addSubview:BtnReferesh];
    } else {
        btnPlusIcon.hidden = NO;
    }
    
    [self GetFeedBackData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

// here we feedback data........
- (void) GetFeedBackData {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@",[KAppDelegate getDictServer:WS_GetFeedback],ToUserId];
        NSLog(@"GetFeedback Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"GetFeedback Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrAllFeedBackData = [json objectForKey:@"Feedback"];
                    
                    if (!arrAsSeller)
                        arrAsSeller = [NSMutableArray new];
                    
                    if (!arrAsBuyer)
                        arrAsBuyer = [NSMutableArray new];
                    
                    for (int i=0; i< arrAllFeedBackData.count; i++) {
                        NSDictionary * dict = [[NSDictionary alloc]init];
                        dict = [arrAllFeedBackData objectAtIndex:i];
                        NSString * str = [dict objectForKey:@"FeedbackType"];
                        
                        if(str != nil) {
                            if(str.intValue == 1) {
                                NSMutableArray * arr = [NSMutableArray arrayWithObject:dict];
                                arrAsSeller = arr;
                            } else if (str.intValue == 2) {
                                NSMutableArray * arr = [NSMutableArray arrayWithObject:dict];
                                arrAsBuyer = arr;
                            }
                        }
                    }
                   [tbl_feedBack reloadData];
                }
            }
        }];
    }
}

#pragma mark - tableView DataSource and Delegate methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if (boolAll) {
        return arrAllFeedBackData.count;
    } else if (boolAsSeller) {
        return arrAsSeller.count;
    } else if (boolAsBuyer) {
        return arrAsBuyer.count;
    }
    
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    FeedBackCell *cell = (FeedBackCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"FeedBackCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [[NSDictionary alloc]init];
    if (boolAll) {
        dict = [arrAllFeedBackData objectAtIndex:indexPath.row];
    } else if (boolAsSeller) {
        dict = [arrAsSeller objectAtIndex:indexPath.row];
    } else if (boolAsBuyer) {
        dict = [arrAsBuyer objectAtIndex:indexPath.row];
    }

    NSString * strUrl = [NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"FeedbackByUserImage"]];
    [obNet SetImageToView:cell.imgUserImage fromImageUrl:strUrl Option:5];
    //[cell.imgUserImage GetNSetUIImage:strUrl DefaultImage:@""];
    
    cell.lblUserName.text = [dict objectForKey:@"FeedbackByUserName"];
    [cell.lblUserName sizeToFit];
    
    cell.lblMessage.text = [dict objectForKey:@"FeedbackDescription"];
    cell.lblMessage.lineBreakMode = NSLineBreakByWordWrapping;
    cell.lblMessage.numberOfLines = 0;
    [cell.lblMessage sizeToFit];
    
    cell.lblFeedBackTime.frame = CGRectMake(cell.lblFeedBackTime.frame.origin.x,cell.lblMessage.frame.origin.y+cell.lblMessage.frame.size.height + 5, cell.lblFeedBackTime.frame.size.width, cell.lblFeedBackTime.frame.size.height);
    cell.lblFeedBackTime.text = [KAppDelegate getTimeDiffrece:[dict objectForKey:@"FeedbackTime"]];
    
    cell.imgClockFeedBackTime.frame = CGRectMake(cell.imgClockFeedBackTime.frame.origin.x,cell.lblMessage.frame.origin.y+cell.lblMessage.frame.size.height + 10, cell.imgClockFeedBackTime.frame.size.width, cell.imgClockFeedBackTime.frame.size.height);
    //cell.imgClockFeedBackTime.image = [UIImage imageNamed:@"clock_icon.png"];
    
    cell.btnFeedBackImage.frame = CGRectMake(cell.btnFeedBackImage.frame.origin.x,cell.lblMessage.frame.origin.y+cell.lblMessage.frame.size.height + 15, cell.btnFeedBackImage.frame.size.width, cell.btnFeedBackImage.frame.size.height);
    
    NSString * FeedBackExp = [dict objectForKey:@"FeedbackExperience"];
    if(FeedBackExp.intValue == 1) {
        cell.imgIcon.image = [UIImage imageNamed:@"positive_icon.png"];
    } else if (FeedBackExp.intValue == 2) {
        cell.imgIcon.image = [UIImage imageNamed:@"neutral_ico.png"];
    } else if (FeedBackExp.intValue == 3) {
        cell.imgIcon.image = [UIImage imageNamed:@"negative_icon.png"];
    }
    
    NSString * FeedbackToUserId = [dict objectForKey:@"FeedbackToUserId"];
    
    if(FeedbackToUserId.intValue == LoginUserId.intValue) {
        cell.btnFeedBackImage.hidden = YES;
        cell.btnEditIconImage.hidden = NO;
    } else {
        cell.btnFeedBackImage.hidden = NO;
        cell.btnEditIconImage.hidden = YES;
    }
    
    NSString * FeedbackUserId = [dict objectForKey:@"FeedbackByUserId"];

    if(FeedbackUserId.intValue == LoginUserId.intValue) {
        cell.btnFeedBackImage.hidden = YES;
        cell.btnEditIconImage.hidden = NO;
       // cell.btnEditIconImage.frame = CGRectMake(cell.btnEditIconImage.frame.origin.x,cell.lblFeedBackTime.frame.origin.y + cell.lblFeedBackTime.frame.size.height, cell.btnEditIconImage.frame.size.width, cell.btnEditIconImage.frame.size.height);
    } else {
        cell.btnFeedBackImage.hidden = NO;
        cell.btnEditIconImage.hidden = YES;
    }
    
    NSString * FeedbackReplyStatus = [dict objectForKey:@"FeedbackReplyStatus"];
    if(FeedbackReplyStatus.intValue == 1) {
        cell.btnEditIconImage.hidden = NO;
        
        cell.btnArrowRight.frame = CGRectMake(cell.btnArrowRight.frame.origin.x,cell.lblFeedBackTime.frame.origin.y + cell.lblFeedBackTime.frame.size.height + 15, cell.btnArrowRight.frame.size.width, cell.btnArrowRight.frame.size.height);
        [cell.btnArrowRight setImage:[UIImage imageNamed:@"arrow_right.png"] forState:UIControlStateNormal];
        
        cell.lblReplyUserName.frame = CGRectMake(cell.lblReplyUserName.frame.origin.x,cell.lblFeedBackTime.frame.origin.y + cell.lblFeedBackTime.frame.size.height + 12, cell.lblReplyUserName.frame.size.width, cell.lblReplyUserName.frame.size.height);
        cell.lblReplyUserName.text = [dict objectForKey:@"FeedbackToUserName"];
        
        cell.lblFeedBackReplyMessage.frame = CGRectMake(cell.lblFeedBackReplyMessage.frame.origin.x,cell.lblFeedBackTime.frame.origin.y + cell.lblFeedBackTime.frame.size.height + 15, cell.lblFeedBackReplyMessage.frame.size.width, cell.lblFeedBackReplyMessage.frame.size.height);
        cell.lblFeedBackReplyMessage.text = [dict objectForKey:@"FeedbackReplyMessage"];
        
        cell.lblFeedBackReplyMessage.lineBreakMode = NSLineBreakByWordWrapping;
        cell.lblFeedBackReplyMessage.numberOfLines = 0;
        [cell.lblFeedBackReplyMessage sizeToFit];
       
        cell.lblFeedBackReplyTime.frame = CGRectMake(cell.lblFeedBackReplyTime.frame.origin.x,cell.lblFeedBackReplyMessage.frame.origin.y + cell.lblFeedBackReplyMessage.frame.size.height + 5, cell.lblFeedBackReplyTime.frame.size.width, cell.lblFeedBackReplyTime.frame.size.height);
        cell.lblFeedBackReplyTime.text = [KAppDelegate getTimeDiffrece:[dict objectForKey:@"FeedbackReplyTime"]];
        
        cell.imgClockReplyTime.frame = CGRectMake(cell.imgClockReplyTime.frame.origin.x,cell.lblFeedBackReplyMessage.frame.origin.y + cell.lblFeedBackReplyMessage.frame.size.height + 10, cell.imgClockReplyTime.frame.size.width, cell.imgClockReplyTime.frame.size.height);
        cell.imgClockReplyTime.image = [UIImage imageNamed:@"clock_icon.png"];
    } else {
    }
    
    NSString * FeedbackEditStatus = [dict objectForKey:@"FeedbackEditStatus"];
    if(FeedbackEditStatus.intValue == 1) {
    } else {
    }
    
    cell.getIndex = indexPath.row;
    cell.delegate = self;
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat height = 0;
    NSDictionary * dict = [arrAllFeedBackData objectAtIndex:indexPath.row];
    NSString * FeedbackReplyStatus = [dict objectForKey:@"FeedbackReplyStatus"];
    if(FeedbackReplyStatus.intValue == 1) {
        height = 110.0f;
    } else {
         height = 45.0f;
    }
    
    CGSize size1 =   [self sizeOfText:[dict objectForKey:@"FeedbackDescription"] widthOfLabel:145 withFont:FONT_Lato_Bold(12.0f)];
    UILabel * lbl1 = [[UILabel alloc]init];
    lbl1.text = [dict objectForKey:@"FeedbackDescription"];
    CGSize size =   [self sizeOfText:[dict objectForKey:@"FeedbackReplyMessage"] widthOfLabel:140 withFont:FONT_Lato_Bold(12.0f)];
    UILabel * lbl = [[UILabel alloc]init];
    lbl.text = [dict objectForKey:@"FeedbackReplyMessage"];
    
    return height + size.height+size1.height;
}

-(CGSize)sizeOfText:(NSString *)textToMesure widthOfLabel:(CGFloat)width withFont:(UIFont*)font
{
    CGSize ts = [textToMesure sizeWithFont:font constrainedToSize:CGSizeMake(width-20.0, FLT_MAX) lineBreakMode:NSLineBreakByWordWrapping];
    return ts;
}

#pragma mark - Button Cell Delegate methods
- (void) FeedBackEditIconButton:(int)index {
    NSDictionary * dict = [[NSDictionary alloc]init];
    if (boolAll) {
        dict = [arrAllFeedBackData objectAtIndex:index];
    } else if (boolAsSeller) {
        dict = [arrAsSeller objectAtIndex:index];
    } else if (boolAsBuyer) {
        dict = [arrAsBuyer objectAtIndex:index];
    }
    
    if(dict.count > 0) {
        
        @try {
            FeedBackId = [dict objectForKey:@"FeedbackId"];
        } @catch (NSException *exception) {}
        
        @try {
            tv_FeedbackEdit.text = [dict objectForKey:@"FeedbackReplyMessage"];
        } @catch (NSException *exception) {}
        
        @try {
            FeedbackType = [dict objectForKey:@"FeedbackType"];
        } @catch (NSException *exception) {}
        
        @try {
            FeedbackExperience = [dict objectForKey:@"FeedbackExperience"];
        } @catch (NSException *exception) {}
        
        @try {
            ReplyStatus = [dict objectForKey:@"FeedbackReplyStatus"];
        } @catch (NSException *exception) {}
    }
    
    NSString *substring = [NSString stringWithString:tv_FeedbackEdit.text];
    lblCounterEditFeedBack.text = [NSString stringWithFormat:@"%lu", 500-substring.length];
    
    if(LoginUserId.intValue == ToUserId.intValue) {
        view_FeedBackEdit.frame = self.view.frame;
        [self.view addSubview:view_FeedBackEdit];
        
    } else {
        static NSInteger dir = 0;
        AddFeedBackScreen * vc = [[AddFeedBackScreen alloc]initWithNibName:@"AddFeedBackScreen" bundle:nil];
        
        @try {
            NSMutableArray * arr = [NSMutableArray arrayWithObject:dict];
            vc.arrFeedbackData = arr;
            vc.dictUserInfo = dictUserInfo;
        } @catch (NSException *exception) {}
       
        dir++;
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
    }
}

// here we call to reply feedback.............
- (void) FeedBackReplyButtonArrow:(int)index {
    NSDictionary * dict = [[NSDictionary alloc]init];
    if (boolAll) {
        dict = [arrAllFeedBackData objectAtIndex:index];
    } else if (boolAsSeller) {
        dict = [arrAsSeller objectAtIndex:index];
    } else if (boolAsBuyer) {
        dict = [arrAsBuyer objectAtIndex:index];
    }
    
    if(dict.count > 0) {
        @try {
            FeedbackByUserId = [dict objectForKey:@"FeedbackByUserId"];
        } @catch (NSException *exception) {}
        
        @try {
            tv_FeedbackReply.text = [dict objectForKey:@"FeedbackDescription"];
        } @catch (NSException *exception) {}
        
        @try {
            ReplyStatus = [dict objectForKey:@"FeedbackReplyStatus"];
        } @catch (NSException *exception) {}
        
        @try {
            FeedBackId = [dict objectForKey:@"FeedbackId"];
        } @catch (NSException *exception) {}
        
        @try {
            FeedbackType = [dict objectForKey:@"FeedbackType"];
        } @catch (NSException *exception) {}
        
        @try {
            FeedbackExperience = [dict objectForKey:@"FeedbackExperience"];
        } @catch (NSException *exception) {}
    }
   
    NSString *substring = [NSString stringWithString:tv_FeedbackReply.text];
    lblCounterReplyFeedBack.text = [NSString stringWithFormat:@"%lu", 500-substring.length];

    view_FeedBackReply.frame = self.view.frame;
    [self.view addSubview:view_FeedBackReply];
}

- (IBAction)BtnRightReplyView:(id)sender {
    
    if(ReplyStatus.intValue == 0) {
        [self AddReplyMethod];
    } else {
        [self EditReplyMethod];
    }
}

- (void) AddReplyMethod {
    NSString * FromUserName = @"";
    NSString * FromUserImage = @"";
    
    if(KAppDelegate.dictLoginInfo.count > 0) {
        @try {
            FromUserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        } @catch (NSException *exception) {}
        
        @try {
            FromUserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FeedbackFromUserId=%@&FeedbackFromUserName=%@&FeedbackFromUserImage=%@&FeedbackToUserId=%@&FeedbackReplyStatus=%@&FeedbackReplyMessage=%@",[KAppDelegate getDictServer:WS_AddFeedback],LoginUserId,FromUserName,FromUserImage,FeedbackByUserId,@"1",tv_FeedbackReply.text];
        NSLog(@"AddFeedback Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"AddFeedback Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [view_FeedBackReply removeFromSuperview];
                    //arrAllFeedBackData = [json objectForKey:@"Feedback"];
                    [self GetFeedBackData];
                    //[tbl_feedBack reloadData];
                } else {
                    [view_FeedBackReply removeFromSuperview];
                    [obNet PopUpMSG:[json objectForKey:@"msg"] Header:@""];
                }
            }
        }];
    }
}

// here we call to edit feedback.............
- (void) EditReplyMethod {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FeedbackId=%@&FeedbackReplyStatus=%@&FeedbackMessage=%@&FeedbackType=%@&FeedbackExperience=%@",[KAppDelegate getDictServer:WS_EditFeedback],FeedBackId,ReplyStatus,tv_FeedbackReply.text,FeedbackType,FeedbackExperience];
        NSLog(@"EditFeedback Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"EditFeedback Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [view_FeedBackReply removeFromSuperview];
                    [view_FeedBackEdit removeFromSuperview];
                    //arrAllFeedBackData = [json objectForKey:@"Feedback"];
                    [self GetFeedBackData];
                    
                    //[tbl_feedBack reloadData];
                } else {
                    [view_FeedBackReply removeFromSuperview];
                    [view_FeedBackEdit removeFromSuperview];
                    [obNet PopUpMSG:[json objectForKey:@"msg"] Header:@""];
                }
            }
        }];
    }
}

- (IBAction)BtnBackReplyView:(id)sender {
    [view_FeedBackReply removeFromSuperview];
}

- (IBAction)BtnRightEditView:(id)sender {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FeedbackId=%@&FeedbackReplyStatus=%@&FeedbackMessage=%@&FeedbackType=%@&FeedbackExperience=%@",[KAppDelegate getDictServer:WS_EditFeedback],FeedBackId,@"0",tv_FeedbackEdit.text,FeedbackType,FeedbackExperience];
        NSLog(@"EditFeedback Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"EditFeedback Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [view_FeedBackEdit removeFromSuperview];
                    //arrAllFeedBackData = [json objectForKey:@"Feedback"];
                    [self GetFeedBackData];
                    //[tbl_feedBack reloadData];
                } else {
                    [view_FeedBackEdit removeFromSuperview];
                    [obNet PopUpMSG:[json objectForKey:@"msg"] Header:@""];
                }
            }
        }];
    }
}

- (IBAction)BtnBackEditView:(id)sender {
    [view_FeedBackEdit removeFromSuperview];
}

- (IBAction)btnPlusIcon:(id)sender {
    static NSInteger dir = 0;
    AddFeedBackScreen * vc = [[AddFeedBackScreen alloc]initWithNibName:@"AddFeedBackScreen" bundle:nil];
    vc.arrFeedbackData = arrAllFeedBackData;
    vc.dictUserInfo = dictUserInfo;
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - Button Actions
- (IBAction)BtnAll:(id)sender {
    [view_AsSeller removeFromSuperview];
    [view_AsBuyer removeFromSuperview];
    view_All.frame = CGRectMake(0, 50, 320, 45);
    [self.view addSubview:view_All];
    
    boolAll = YES;
    boolAsBuyer = NO;
    boolAsSeller = NO;
    
    [tbl_feedBack reloadData];
}

- (IBAction)BtnAsSeller:(id)sender {
    [view_All removeFromSuperview];
    [view_AsBuyer removeFromSuperview];
    view_AsSeller.frame = CGRectMake(0, 50, 320, 45);
    [self.view addSubview:view_AsSeller];
    
    boolAll = NO;
    boolAsBuyer = NO;
    boolAsSeller = YES;
    
    [tbl_feedBack reloadData];
}

- (IBAction)BtnAsBuyer:(id)sender {
    [view_All removeFromSuperview];
    [view_AsSeller removeFromSuperview];
    view_AsBuyer.frame = CGRectMake(0, 50, 320, 45);
    [self.view addSubview:view_AsBuyer];
    
    boolAll = NO;
    boolAsBuyer = YES;
    boolAsSeller = NO;

    [tbl_feedBack reloadData];
}

- (IBAction)BtnReferesh:(id)sender {
    [self GetFeedBackData];
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - textView Delegate methods
- (void)textViewDidChange:(UITextView *)textView {
    
    NSString *substring = [NSString stringWithString:tv_FeedbackReply.text];
    NSString *substring1 = [NSString stringWithString:tv_FeedbackEdit.text];
    
    if (substring.length > 0) {
        lblCounterReplyFeedBack.text = [NSString stringWithFormat:@"%lu", 500-substring.length];
        lblCounterEditFeedBack.text = [NSString stringWithFormat:@"%lu", 500-substring1.length];
    }
    
    if (substring.length == 500 || substring.length > 500) {
        [textView resignFirstResponder];
    }
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    return YES;
}
    
@end
