//
//  Followers.m
//  Reloved
//
//  Created by Kamal on 24/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "Followers.h"
#import "TableViewCell.h"

@interface Followers ()

@end

@implementation Followers
{
    NSString * ProfileId;
    NSMutableArray * arrFollows;
}
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
    arrFollower = [[NSMutableArray alloc]init];
    arrFollows  = [[NSMutableArray alloc]init];
    tbl_Follower.dataSource = self;
    tbl_Follower.delegate = self;
    
    lblFollowers.font = FONT_Lato_Bold(20.0f);
    ProfileId = @"";
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    intPage = 1;
    NSLog(@"DictProfileData--> %@",DictProfileData);
    
    if(DictProfileData.count > 0){
        ProfileId = [DictProfileData objectForKey:@"UserId"];
    }
    
    [self DownLoadGetFollowerData];
}

// here we get follower data.......
- (void) DownLoadGetFollowerData {

    NSString * UserId = @"";
    @try {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&page=%d&ProfileId=%@",[KAppDelegate getDictServer:WS_GetFollower],UserId,intPage,ProfileId];
        NSLog(@"GetFollower Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetFollower Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrFollower = [json objectForKey:@"Followers"];
                    [tbl_Follower reloadData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

#pragma mark - TableView datasource and Delegate methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrFollower.count;
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    TableViewCell *cell = (TableViewCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"TableViewCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrFollower objectAtIndex:indexPath.row];
    
    NSString * userId = [dict objectForKey:@"UserId"];
    NSString * loginUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    
    if(userId.intValue == loginUserId.intValue) {
        cell.BtnFollow.hidden = YES;
    } else {
        cell.BtnFollow.hidden = NO;
    }
    
   NSString * FollowStatus =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"FollowStatus"] intValue]];
    NSLog(@"FollowStatus-->%@",FollowStatus);
    if(FollowStatus.intValue == 1) {
        [cell.BtnFollow setTitle:@"Following" forState:UIControlStateNormal];
    } else {
        [cell.BtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
    }

    if (indexPath.row == [arrFollower count]-1) {
        if (!boolPagination) {
            boolPagination = YES;
            
            if(arrFollower.count % 10 ==0) {
                intPage++;
                [self DownLoadGetFollowerData];
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
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 67.0f;
}

// here we Follow and unfollow..........
- (void) FollowerAndUnFollower:(int)indexValue {
    
    NSDictionary * dict = [arrFollower objectAtIndex:indexValue];
    
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
                        arr = [arrFollower mutableCopy];
                        NSMutableDictionary * ddd = [[NSMutableDictionary alloc]init];
                        
                        ddd = [dict mutableCopy];
                        [ddd setObject:Status forKey:@"FollowStatus"];
                        [arr replaceObjectAtIndex:indexValue withObject:ddd];
                        arrFollower = arr;
                        
                        [tbl_Follower reloadData];
                    } @catch (NSException *exception) {}
                    
                    //[self DownLoadGetFollowerData];
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
    [self DownLoadGetFollowerData];
    [tbl_Follower reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
