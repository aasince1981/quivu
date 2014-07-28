//
//  FollowingViewController.m
//  Reloved
//
//  Created by Kamal on 16/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "FollowingViewController.h"
#import "FollowingTabCustomCell.h"
#import "ProfileOther.h"

@interface FollowingViewController ()

@end

@implementation FollowingViewController

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
    [self.navigationController setNavigationBarHidden:YES];
    
    tbl_Follow.delegate = self;
    tbl_Follow.dataSource = self;
    arrFollowingUser = [[NSMutableArray alloc]init];
    intPage = 1;
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    boolPagination = YES;
    [self DownLoadGetFollowingData];
}

// here we get following data.......
- (void) DownLoadGetFollowingData {
    
    NSString * UserId = @"";
    @try {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&page=%d&ProfileId=%@",[KAppDelegate getDictServer:WS_GetFollowing],UserId,intPage,UserId];
        NSLog(@"GetFollowing Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetFollowing Json value-->%@",json);
            if(json != nil) {
                
                if(IS_IPHONE_5) {
                    [view_PopUp removeFromSuperview];
                } else {
                    [view_PopUp removeFromSuperview];
                }
                
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrFollowingUser = [json objectForKey:@"Following"];
                    [tbl_Follow reloadData];
                    
                    if(IS_IPHONE_5) {
                        [view_PopUp removeFromSuperview];
                    } else {
                        [view_PopUp removeFromSuperview];
                    }
                } else {
                    arrFollowingUser = [[NSMutableArray alloc]init];
                    if(IS_IPHONE_5) {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 518);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    } else {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 430);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    }
                    [tbl_Follow reloadData];
                }
            } else {
                [[LoadingViewController instance] stopRotation];
                if(IS_IPHONE_5) {
                    view_PopUp.frame = CGRectMake(0, 50, 320, 518);
                    lblMessage.font = [UIFont systemFontOfSize:17.0];
                    lblMessage.text = [NSString stringWithFormat:@"%@",kCouldnotconnectServer];
                    [self.view addSubview:view_PopUp];
                } else {
                    lblMessage.font = [UIFont systemFontOfSize:17.0];
                    view_PopUp.frame = CGRectMake(0, 50, 320, 430);
                    lblMessage.text = [NSString stringWithFormat:@"%@",kCouldnotconnectServer];
                    [self.view addSubview:view_PopUp];
                }
                [tbl_Follow reloadData];
            }
        }];
    } else {
        [[LoadingViewController instance] stopRotation];
        if(IS_IPHONE_5) {
            view_PopUp.frame = CGRectMake(0, 50, 320, 518);
            lblMessage.font = [UIFont systemFontOfSize:17.0];
            lblMessage.text = [NSString stringWithFormat:@"%@",kInternetNotAvailable];
            [self.view addSubview:view_PopUp];
        } else {
            lblMessage.font = [UIFont systemFontOfSize:17.0];
            view_PopUp.frame = CGRectMake(0, 50, 320, 430);
            lblMessage.text = [NSString stringWithFormat:@"%@",kInternetNotAvailable];
            [self.view addSubview:view_PopUp];
        }
        [tbl_Follow reloadData];
    }
}

#pragma mark - tableView dataSource Methods
- (NSInteger) numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrFollowingUser.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    FollowingTabCustomCell *cell = (FollowingTabCustomCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"FollowingTabCustomCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [[NSDictionary alloc]init];
    dict = [arrFollowingUser objectAtIndex:indexPath.row];
    
    NSString * userId = [dict objectForKey:@"UserId"];
    NSString * loginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    if(userId.intValue == loginUserId.intValue) {
        cell.BtnFollow.hidden = YES;
    } else {
        cell.BtnFollow.hidden = NO;
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    NSString * FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowingStatus"] intValue]];
    
    if(FollowStatus.intValue == 1) {
        [cell.BtnFollow setTitle:@"Following" forState:UIControlStateNormal];
    } else {
        [cell.BtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
    }
    
    if (indexPath.row == [arrFollowingUser count]-1) {
        if (!boolPagination) {
            boolPagination = YES;
            
            if(arrFollowingUser.count % 10 == 0) {
                intPage++;
                [self DownLoadGetFollowingData];
            }
        }
    }
    
    cell.delegate = self;
    cell.getIndex = indexPath.row;
    
    NSString * str =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"UserImage"]];
    if(str != nil) {
        [obNet SetImageToView:cell.img_ProfileImage fromImageUrl:str Option:5];
    }
    cell.BtnFollow.titleLabel.font = FONT_Lato_Bold(16.0f);
    cell.lbl_UserName.font = FONT_Lato_Bold(16.0f);
    cell.lbl_UserName.text = [dict objectForKey:@"UserName"];
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary * dict = [arrFollowingUser objectAtIndex:indexPath.row];
    NSString * userId = [dict objectForKey:@"UserId"];
    
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

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 67.0f;
}

// here we Follow and unfollow..........
- (void) FollowAndFollowingMethod:(int)indexValue {
    
    NSDictionary * dict = [arrFollowingUser objectAtIndex:indexValue];
    
    NSString * FromUserId =@"";
    NSString * FromUserName =@"";
    NSString * FromUserImage =@"";
    NSString * ToUserId =@"";
    NSString * ToUserName =@"";
    NSString * ToUserImage =@"";
    NSString * Status = @"";
    
    NSString  *FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowingStatus"] intValue]];
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
                        arr = [arrFollowingUser mutableCopy];
                        NSMutableDictionary * ddd = [[NSMutableDictionary alloc]init];
                        ddd = [dict mutableCopy];
                        [ddd setObject:Status forKey:@"FollowingStatus"];
                        [arr replaceObjectAtIndex:indexValue withObject:ddd];
                        arrFollowingUser = arr;
                        [tbl_Follow reloadData];
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

- (IBAction)btnReferesh:(id)sender {
    [self DownLoadGetFollowingData];
    [tbl_Follow reloadData];
}

@end
