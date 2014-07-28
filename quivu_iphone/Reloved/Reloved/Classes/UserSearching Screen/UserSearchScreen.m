//
//  UserSearchScreen.m
//  quivu
//
//  Created by Kamal on 05/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "UserSearchScreen.h"
#import "ProfileOther.h"

@interface UserSearchScreen ()

@end

@implementation UserSearchScreen

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    arrSearchUserList = [[NSMutableArray alloc]init];
    
    tbl_SearchUser.dataSource = self;
    tbl_SearchUser.delegate = self;
    
    [tf_Search setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
     tf_Search.delegate = self;
}

#pragma mark - viewWillAppear method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    btnSearchIcon.enabled = NO;
}

- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_Search.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
}

#pragma mark - tableView Delegate and DataSource method
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrSearchUserList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    SearchUserCustomCell *cell = (SearchUserCustomCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"SearchUserCustomCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrSearchUserList objectAtIndex:indexPath.row];
    
    NSString * userId = [dict objectForKey:@"UserId"];
    NSString * loginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    if(userId.intValue == loginUserId.intValue) {
        cell.BtnFollow.hidden = YES;
    } else {
        cell.BtnFollow.hidden = NO;
    }
    
    NSString * FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowStatus"] intValue]];
    
    if(FollowStatus.intValue == 1) {
        [cell.BtnFollow setTitle:@"Following" forState:UIControlStateNormal];
    } else {
        [cell.BtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
    }
    
    /*
    if (indexPath.row == [arrSearchUserList count]-1) {
        if (!boolPagination) {
            boolPagination = YES;
            
            if(arrSearchUserList.count % 10 ==0) {
                intPage++;
                [self DownLoadGetFollowerData];
            }
        }
    }
     */
    
    cell.delegate = self;
    cell.getIndex = indexPath.row;
    
    NSString * str =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"UserImage"]];
    if(str != nil) {
        [obNet SetImageToView:cell.img_ProfileImage fromImageUrl:str Option:5];
    }
    
    cell.BtnFollow.titleLabel.font = FONT_Lato_Bold(16.0f);
    cell.lbl_UserName.font = FONT_Lato_Bold(16.0f);
    cell.lbl_UserName.text = [dict objectForKey:@"UserName"];
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary * dict = [arrSearchUserList objectAtIndex:indexPath.row];
    NSString * userId = [dict objectForKey:@"UserId"];
    NSString * LoginId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    
    @try {
        if(LoginId.intValue == userId.intValue) {
            
        } else {
            static NSInteger dir = 0;
            ProfileOther * profile = [[ProfileOther alloc]initWithNibName:@"ProfileOther" bundle:nil];
            dir++;
            
            if(userId != nil) {
                profile.ProductUserId = [NSString stringWithFormat:@"%@",userId];
            }
            
            KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
            profile.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:profile animated:YES];
        }
    } @catch (NSException *exception) {}
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 67.0f;
}

// here follow and unfollow to user.....................
- (void) FollowAndUnFollowUserSearch:(int)indexValue {
    NSLog(@"%d",indexValue);
    
    NSDictionary * dict = [arrSearchUserList objectAtIndex:indexValue];
    
    NSString * FromUserId =@"";
    NSString * FromUserName =@"";
    NSString * FromUserImage =@"";
    NSString * ToUserId =@"";
    NSString * ToUserName =@"";
    NSString * ToUserImage =@"";
    NSString * Status = @"";
    
    NSString  *FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowStatus"] intValue]];
    if(FollowStatus.intValue == 1) {
        Status = @"0";
    } else {
        Status = @"1";
    }
    
    if(KAppDelegate.dictLoginInfo.count > 0){
        @try {
            FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
        } @catch (NSException *exception) { }
        
        @try {
            FromUserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        } @catch (NSException *exception) { }
        
        @try {
            if([KAppDelegate.dictLoginInfo objectForKey:@"UserImage"] != nil) {
                FromUserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
            } else {
                FromUserImage = @"";
            }
            
        } @catch (NSException *exception) { }
    }
    
    if(dict.count > 0) {
        @try {
            ToUserId = [dict objectForKey:@"UserId"];
        } @catch (NSException *exception) {}
        
        @try {
            ToUserName = [dict objectForKey:@"UserName"];
        } @catch (NSException *exception) {}
        
        @try {
            ToUserImage = [dict objectForKey:@"UserImage"];
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FromUserId=%@&FromUserName=%@&FromUserImage=%@&ToUserId=%@&ToUserName=%@&ToUserImage=%@&FollowStatus=%@",[KAppDelegate getDictServer:WS_AddFollow],FromUserId,FromUserName,FromUserImage,ToUserId,ToUserName,ToUserImage,Status];
        NSLog(@"AddFollow Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddFollow Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    
                    @try {
                        NSMutableArray * arr = [[NSMutableArray alloc]init];
                        arr = [arrSearchUserList mutableCopy];
                        NSMutableDictionary * ddd = [[NSMutableDictionary alloc]init];
                        
                        ddd = [dict mutableCopy];
                        [ddd setObject:Status forKey:@"FollowStatus"];
                        [arr replaceObjectAtIndex:indexValue withObject:ddd];
                        arrSearchUserList = arr;
                        
                        [tbl_SearchUser reloadData];
                    } @catch (NSException *exception) {}
                    
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - Button Actions
- (IBAction)BtnSearchIcon:(id)sender {
    [self GetUserList];
}

// here we get user list........................
- (void) GetUserList {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserName=%@&UserId=%@",[KAppDelegate getDictServer:WS_SearchUsers],tf_Search.text,[KAppDelegate.dictLoginInfo objectForKey:@"UserId"]];
        NSLog(@"SearchUsers Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"SearchUsers Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    tf_Search.text = @"";
                    arrSearchUserList = [json objectForKey:@"Users"];
                    if(IS_IPHONE_5) {
                        [view_PopUp removeFromSuperview];
                    } else {
                        [view_PopUp removeFromSuperview];
                    }
                    [tbl_SearchUser reloadData];
                    
                } else {
                    tf_Search.text = @"";
                    arrSearchUserList = [[NSMutableArray alloc]init];
                    if(IS_IPHONE_5) {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 518);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    } else {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 430);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    }
                    
                    [tbl_SearchUser reloadData];
                }
            }
        }];
    }
}

- (IBAction)btnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - textField Delegate methods

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    
    [textField resignFirstResponder];
    return YES;
}

-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    NSUInteger length = tf_Search.text.length - range.length + string.length;
    if (length > 0) {
        btnSearchIcon.enabled = YES;
    } else {
        btnSearchIcon.enabled = NO;
    }
    return YES;
}

@end
