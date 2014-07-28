//
//  ActivityViewController.m
//  Reloved
//
//  Created by Kamal on 16/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ActivityViewController.h"
#import "ItemDetails.h"
#import "ViewChatViewController.h"
#import "ProfileOther.h"
#import "FeedBackScreen.h"

@interface ActivityViewController ()

@end

@implementation ActivityViewController
@synthesize arrActivities;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

#pragma mark -  View LifeCycle:
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:YES];
    tblView.dataSource = self;
    tblView.delegate = self;
}

#pragma mark - viewWillAppear method
- (void) viewWillAppear:(BOOL)animated {
    arrActivities = [[NSMutableArray alloc] init];
    @try {
        strUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    KAppDelegate.MyVC = self;
    intPage = 1;
    flagPagination = YES;
    ActivityTypeId = @"";
    [self performSelectorInBackground:@selector(getActivityList:) withObject:strUserId];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - Button actions
- (IBAction)btn_Refresh:(id)sender {
    arrActivities = [[NSMutableArray alloc] init];
    intPage = 1;
    flagPagination = YES;
    
    @try {
        [self getActivityList:strUserId];
    } @catch (NSException *exception) { }
}

#pragma mark - Get Activity List Function :
// getActicity list here.....

- (void) getActivityList:(NSString *) userId {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&page=%d&UserId=%@",[KAppDelegate getDictServer:WS_GetActivites],intPage,userId];
        NSLog(@"GetActivites Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetActivites Json value-->%@",json);
            if(json != nil) {
                if(IS_IPHONE_5) {
                    [view_PopUp removeFromSuperview];
                } else {
                    [view_PopUp removeFromSuperview];
                }
                
                if([[json objectForKey:@"success"] intValue] == 1) {
                    
                    // code for paging here.......
                    @try {
                        NSMutableArray *arrData = [json objectForKey:@"Activities"];
                        if(arrData.count > 0) {
                            if(arrData.count == 1 && intPage ==1){
                                arrActivities = arrData;
                            } else {
                                // If data greater than 1 then add in array.
                                for(int i = 0; i< arrData.count; i++){
                                    NSDictionary * dict = [arrData objectAtIndex:i];
                                    [arrActivities addObject:dict];
                                }
                            }
                            flagPagination = NO;
                            [tblView reloadData];
                        }
                    } @catch (NSException *exception) {}
                    
                    if(IS_IPHONE_5) {
                        [view_PopUp removeFromSuperview];
                    } else {
                        [view_PopUp removeFromSuperview];
                    }
                } else {
                    arrActivities = [[NSMutableArray alloc]init];
                    if(IS_IPHONE_5) {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 518);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    } else {
                        view_PopUp.frame = CGRectMake(0, 50, 320, 430);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    }
                    [tblView reloadData];
                }
            } else {
                [[LoadingViewController instance] stopRotation];
                arrActivities = [[NSMutableArray alloc]init];
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
                [tblView reloadData];
            }
        }];
    } else {
        arrActivities = [[NSMutableArray alloc]init];
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
        [tblView reloadData];
    }
}

- (IBAction)btn_Search:(id)sender {
}

#pragma mark - TableView DataSource and Delegate methods
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrActivities.count;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *strCell = @"cell";
    ActivityCell *cell = (ActivityCell*)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"ActivityCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrActivities objectAtIndex:indexPath.row];
    ActivityTypeId = [dict objectForKey:@"ActivityTypeId"];
   
    if(ActivityTypeId.intValue == 1) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"green_like_icon.png"]];
    } else if (ActivityTypeId.intValue == 2) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"small_arrow_icon.png"]];
    } else if (ActivityTypeId.intValue == 3) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"small_message_icon.png"]];
    } else if (ActivityTypeId.intValue == 5) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"red_small_arrow.png"]];
    } else if (ActivityTypeId.intValue == 7) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"user_small_icon.png"]];
    } else if (ActivityTypeId.intValue == 8) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"user_small_icon.png"]];
    } else if (ActivityTypeId.intValue == 9) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"green_like_icon.png"]];
    } else if (ActivityTypeId.intValue == 10) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"green_like_icon.png"]];
    } else if (ActivityTypeId.intValue == 11) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"small_chat_icon.png"]];
    } else if (ActivityTypeId.intValue == 13) {
        [cell.imgIcon setImage:[UIImage imageNamed:@"delete_icon_small.png"]];
    }
    
    if (indexPath.row == [arrActivities count]-1) {
        if (!flagPagination) {
            flagPagination = YES;
            if(arrActivities.count % 10 == 0) {
                intPage++;
                [self performSelectorInBackground:@selector(getActivityList:) withObject:strUserId];
            }
        }
    }
    
    NSString * imgUrl = [NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"ActivityFromUserImage"]];
    
    NSString * imgProductUrl = [NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[dict objectForKey:@"ActivityProductImage"]];
    [cell.imgView_UserImage GetNSetUIImage:imgUrl DefaultImage:@""];
    [cell.imgView_ProductImage GetNSetUIImage:imgProductUrl DefaultImage:@""];
    
    //[obNet SetImageToView:cell.imgView_UserImage fromImageUrl:imgUrl Option:5];
   // [obNet SetImageToView:cell.imgView_ProductImage fromImageUrl:imgProductUrl Option:5];
    
    NSString * userName = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ActivityFromUserName"]];
    NSString *lblUserSms = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ActivityMessage"]];
    
    if(lblUserSms.length > 0) {
        cell.lbl_UserName_Msg.text = [lblUserSms stringByReplacingOccurrencesOfString:userName withString:@""];
    }
    cell.lbl_UserName_Msg.numberOfLines = 0;
    cell.lbl_UserName_Msg.lineBreakMode = NSLineBreakByWordWrapping;
    [cell.lbl_UserName_Msg sizeToFit];
    
    cell.imgClockIcon.frame = CGRectMake(cell.imgClockIcon.frame.origin.x,cell.lbl_UserName_Msg.frame.origin.y + cell.lbl_UserName_Msg.frame.size.height + 3, cell.imgClockIcon.frame.size.width, cell.imgClockIcon.frame.size.height);
    cell.lbl_LastLogin.frame = CGRectMake(cell.lbl_LastLogin.frame.origin.x,cell.lbl_UserName_Msg.frame.origin.y + cell.lbl_UserName_Msg.frame.size.height, cell.lbl_LastLogin.frame.size.width, cell.lbl_LastLogin.frame.size.height);
    
    [cell.btnUserName setTitle:userName forState:UIControlStateNormal];
    cell.lbl_LastLogin.text =[KAppDelegate getTimeDiffrece:[dict objectForKey:@"ActivityTime"]];
    
    cell.getIndex = indexPath.row;
    cell.delegate = self;
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

// this method is click on tableView cell for particulatr index......
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {

    NSDictionary * dict = [arrActivities objectAtIndex:indexPath.row];
    ActivityTypeId = [dict objectForKey:@"ActivityTypeId"];
    NSString * ProductId = [dict objectForKey:@"ActivityProductId"];
    NSString * UserId = [dict objectForKey:@"ActivityFromUserId"];
    //NSString * LoginId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    
    static NSInteger dir = 0;
    ItemDetails * Details = [[ItemDetails alloc]initWithNibName:@"ItemDetails" bundle:nil];
    ViewChatViewController * viewChat = [[ViewChatViewController alloc]initWithNibName:@"ViewChatViewController" bundle:nil];
    FeedBackScreen * feedback = [[FeedBackScreen alloc]initWithNibName:@"FeedBackScreen" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    Details.hidesBottomBarWhenPushed = YES;
    Details.ProductId = [NSString stringWithFormat:@"%@",ProductId];
    
    viewChat.hidesBottomBarWhenPushed = YES;
    viewChat.ProductId = [NSString stringWithFormat:@"%@",ProductId];
    
    feedback.hidesBottomBarWhenPushed = YES;
    
    @try {
        NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
        [dd setObject:UserId forKey:@"UserId"];
        feedback.dictUserInfo = dd;
    } @catch (NSException *exception) {}
    
    if(ActivityTypeId.intValue == 1) {
        [self.navigationController pushViewController:Details animated:NO];
        
    } else if (ActivityTypeId.intValue == 2) {
        [self.navigationController pushViewController:Details animated:NO];
        
    } else if (ActivityTypeId.intValue == 3) {
        [self.navigationController pushViewController:Details animated:NO];
        
    } else if (ActivityTypeId.intValue == 4) {
        [self.navigationController pushViewController:viewChat animated:NO];
        
    } else if (ActivityTypeId.intValue == 5) {
        [self.navigationController pushViewController:Details animated:NO];
        
    } else if (ActivityTypeId.intValue == 6) {
        [self.navigationController pushViewController:Details animated:NO];
        
    } else if (ActivityTypeId.intValue == 7) {
        [self.navigationController pushViewController:feedback animated:NO];
        
    } else if (ActivityTypeId.intValue == 8) {
        [self.navigationController pushViewController:feedback animated:NO];
        
    } else if (ActivityTypeId.intValue == 11) {
        [self.navigationController pushViewController:Details animated:NO];
        
    } else if (ActivityTypeId.intValue == 12) {
        [self.navigationController pushViewController:viewChat animated:NO];
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 90;
}

// Button Call on click UserName......
- (void) btnUserNameClick: (int) indexValue {
    NSDictionary * dict = [arrActivities objectAtIndex:indexValue];
    NSString * UserId = [dict objectForKey:@"ActivityFromUserId"];
    NSString * LoginId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    
    static NSInteger dir = 0;
    ProfileOther * profile = [[ProfileOther alloc]initWithNibName:@"ProfileOther" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    profile.hidesBottomBarWhenPushed = YES;
    profile.ProductUserId = [NSString stringWithFormat:@"%@",UserId];
    
    if(LoginId.intValue == UserId.intValue) {
    } else {
        [self.navigationController pushViewController:profile animated:NO];
    }
}

@end
