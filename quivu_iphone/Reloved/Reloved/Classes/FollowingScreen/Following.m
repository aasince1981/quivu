//
//  Following.m
//  Reloved
//
//  Created by Kamal on 23/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "Following.h"
#import "FollowingCustomCell.h"

@interface Following ()

@end

@implementation Following

@synthesize DictProfileData;

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
    tbl_Follow.delegate = self;
    tbl_Follow.dataSource = self;
    ProfileId = @"";
    arrFollowingUser = [[NSMutableArray alloc]init];
    intPage = 1;
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    if(DictProfileData.count > 0){
        ProfileId = [DictProfileData objectForKey:@"UserId"];
    }
    boolPagination = YES;
    [self DownLoadGetFollowingData];
    
    [tbl_Follow reloadData];
}

// here we get following data.......
- (void) DownLoadGetFollowingData {
    NSString * UserId = @"";
    @try {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&page=%d&ProfileId=%@",[KAppDelegate getDictServer:WS_GetFollowing],UserId,intPage,ProfileId];
        NSLog(@"GetFollowing Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetFollowing Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrFollowingUser = [json objectForKey:@"Following"];
                    [tbl_Follow reloadData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

#pragma mark - tableView dataSource Methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrFollowingUser.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    FollowingCustomCell *cell = (FollowingCustomCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"FollowingCustomCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [[NSDictionary alloc]init];
    dict = [arrFollowingUser objectAtIndex:indexPath.row];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    NSString * FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowingStatus"] intValue]];
    
    NSString * userId = [dict objectForKey:@"UserId"];
    NSString * loginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    
    if(userId.intValue == loginUserId.intValue) {
        cell.BtnFollow.hidden = YES;
    } else {
        cell.BtnFollow.hidden = NO;
    }
    
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

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 67.0f;
}

// here we Follow and unfollow..........
- (void) FollowAndUnFollow:(int)indexValue {
    
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

#pragma mark - button Actions
- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)BtnReferesh:(id)sender {
    [self DownLoadGetFollowingData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
