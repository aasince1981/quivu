//
//  ViewOfferScreen.m
//  Reloved
//
//  Created by Kamal on 28/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ViewOfferScreen.h"
#import "ViewChatViewController.h"
#import "ProfileOther.h"

@interface ViewOfferScreen ()
{
    NSString * ProductId;
    NSString * ProductName;
    NSString * ProductImage;
}

@end

@implementation ViewOfferScreen
@synthesize DictProductDetails;

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
    
    lblHeader.font = FONT_Lato_Bold(20.0f);
    lblListedPrice.font = FONT_Lato_Light(16.0f);
    lblProductName.font = FONT_Lato_Bold(18.0f);
    lblProductPrice.font = FONT_Lato_Bold(18.0f);
    
    arrOfferList = [[NSMutableArray alloc]init];
    
    tbl_ViewOffer.dataSource = self;
    tbl_ViewOffer.delegate = self;
    ProductId = @"";
    ProductName = @"";
}

- (void) viewWillAppear:(BOOL)animated {
   KAppDelegate.MyVC = self;
    [self SetValue];
    [self GetOfferList];
}

// here we set value to view..................
- (void) SetValue {
    NSLog(@"DictProductDetails-->%@",DictProductDetails);
    if(DictProductDetails.count > 0) {
        ProductId = [DictProductDetails objectForKey:@"ProductId"];
    }
    
    NSArray * arr = [DictProductDetails objectForKey:@"ProductImageInfo"];
    if(arr.count > 0) {
        //for(int i = 0; i < arr.count; i++) {
            NSString  * strImageShareUrl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arr objectAtIndex:0] objectForKey:@"ProImageName"]];
            
            [obNet SetImageToView:imgProductImage fromImageUrl:strImageShareUrl Option:5];
        
        ProductImage = [[arr objectAtIndex:0] objectForKey:@"ProImageName"];
       // }
    }
    
    @try {
        lblProductPrice.text = [NSString stringWithFormat:@"$ %@", [DictProductDetails objectForKey:@"ProductPrice"]];
    } @catch (NSException *exception) {}
    
    @try {
        lblProductName.text = [NSString stringWithFormat:@"%@",[DictProductDetails objectForKey:@"ProductName"]];
        ProductName = [DictProductDetails objectForKey:@"ProductName"];
    } @catch (NSException *exception) {}
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

// here we get offer list.................
- (void) GetOfferList {
  
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&OfferProductId=%@",[KAppDelegate getDictServer:WS_GetOffers],ProductId];
        NSLog(@"GetOffers Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetOffers Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrOfferList = [json objectForKey:@"offers"];
                    [tbl_ViewOffer reloadData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
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
    return arrOfferList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    ViewOfferCell *cell = (ViewOfferCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"ViewOfferCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrOfferList objectAtIndex:indexPath.row];
    
    cell.lblUserName.text = [NSString stringWithFormat:@"%@",[dict objectForKey:@"OfferUserName"]];
    cell.lblProductPrice.text = [NSString stringWithFormat:@"$ %@",[dict objectForKey:@"OfferAmount"]];
    cell.lblTimeDuration.text = [NSString stringWithFormat:@"%@",[KAppDelegate getTimeDiffrece:[dict objectForKey:@"OfferAddTime"]]];
    
    @try {
        NSString * str =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"OfferUserImage"]];
        [obNet SetImageToView:cell.imgUserImage fromImageUrl:str Option:5];
    } @catch (NSException *exception) {}
    
    cell.getIndex = indexPath.row;
    cell.delegate = self;
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary * dict = [arrOfferList objectAtIndex:indexPath.row];
    
    NSString * strId;
    NSString * LoginId;
    @try {
        strId = [dict objectForKey:@"OfferUserId"];
    } @catch (NSException *exception) {}
    
    @try {
        LoginId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if(LoginId.intValue == strId.intValue) {
        NSLog(@"equal Id");
    } else {
        static NSInteger dir = 0;
        ProfileOther * profile = [[ProfileOther alloc]initWithNibName:@"ProfileOther" bundle:nil];
        dir++;
        
        if(strId != nil) {
            profile.ProductUserId = [NSString stringWithFormat:@"%@",strId];
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        profile.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:profile animated:YES];
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 65.0f;
}

- (void) BtnViewOfferAction:(int)indexValue {
    
    NSString * offerId = @"";
    NSDictionary * dict = [arrOfferList objectAtIndex:indexValue];
    
    if(dict.count > 0) {
        offerId = [dict objectForKey:@"OfferUserId"];
    }
    
    static NSInteger dir = 0;
    ViewChatViewController * vc = [[ViewChatViewController alloc]initWithNibName:@"ViewChatViewController" bundle:nil];
    dir++;
    
    vc.ProductId = [NSString stringWithFormat:@"%@",ProductId];
    NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
    dd = [dict mutableCopy];
    [dd setObject:ProductName forKey:@"ProductName"];
    [dd setObject:ProductImage forKey:@"ProductImage"];
    
    vc.dictOfferDetails = dd;
    vc.boolCancelOffer = YES;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - Button actions
- (IBAction)BtnReferesh:(id)sender {
    [self GetOfferList];
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
 
@end
