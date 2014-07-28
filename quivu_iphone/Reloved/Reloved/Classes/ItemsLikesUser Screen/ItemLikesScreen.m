//
//  ItemLikesScreen.m
//  Reloved
//
//  Created by Kamal on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ItemLikesScreen.h"
#import "ProfileOther.h"

@interface ItemLikesScreen ()

@end

@implementation ItemLikesScreen
@synthesize ProductId;

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
    
     lblItemLikes.font = FONT_Lato_Bold(20.0f);
    tbl_LikesUser.dataSource = self;
    tbl_LikesUser.delegate = self;
    
    arrItemLikesUser = [[NSMutableArray alloc]init];
}

#pragma mark - viewWillAppear method

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    NSLog(@"ProductId--> %@",ProductId);
    
    [self GetItemsLikeUsers];
}

// here we get itemsuserLike data...........
- (void) GetItemsLikeUsers {
    
    NSString * UserId = @"";
    if(KAppDelegate.dictLoginInfo.count > 0) {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    }
       
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@&UserId=%@",[KAppDelegate getDictServer:WS_GetLikesUsers],ProductId,UserId];
        NSLog(@"getLikesUsers Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"getLikesUsers Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrItemLikesUser = [json objectForKey:@"Users"];
                    [tbl_LikesUser reloadData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

#pragma mark - tableView DataSource and Delegates methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrItemLikesUser.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    ItemsLikeCell *cell = (ItemsLikeCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"ItemsLikeCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrItemLikesUser objectAtIndex:indexPath.row];
    
    dict = [arrItemLikesUser objectAtIndex:indexPath.row];
    
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
    NSDictionary * dict = [arrItemLikesUser objectAtIndex:indexPath.row];
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

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80.0f;
}

#pragma mark - Button Actions

- (IBAction)BtnReferesh:(id)sender {
    [self GetItemsLikeUsers];
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - FollowAndUnFollow data
// here follow and unFollow...................
- (void) FollowAndFollowingLikeUser:(int)indexValue {
    
    NSDictionary * dict = [arrItemLikesUser objectAtIndex:indexValue];
    
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
                    [self GetItemsLikeUsers];
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

@end
