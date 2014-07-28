//
//  CameraTest.h
//  myFotocloset
//
//  Created by Ideal IT Techno on 19/04/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "CameraEdit.h"
//#import "Home.h"
#import <AVFoundation/AVFoundation.h>
#import <AviarySDK/AviarySDK.h>

@interface CameraTest : UIViewController <UIImagePickerControllerDelegate, UINavigationControllerDelegate, UIGestureRecognizerDelegate,AFPhotoEditorControllerDelegate>
{
    IBOutlet UIView *viewUp;
    IBOutlet UIView *viewUpBottom;
    IBOutlet UIView *view_images;
    IBOutlet UIScrollView *sc_images;
    
    IBOutlet UIButton *btnViewImages;
    UIImagePickerController *imagePickerController;
    
    float lastZoomScal;
    
    BOOL boolFrontCamera;
    
    IBOutlet UIView *view_camera;
    IBOutlet UIButton *btniPhone5LeftRight;
    IBOutlet UIButton *btniPhone5TopDown;
    
    BOOL flagStore;
    BOOL isSound;
    BOOL isFlashOn;
    int lastRotation;
    BOOL boolRotateScreen;
    
    BOOL boolPickImage;
    //CameraEdit * cameraEdit;
}

@property (strong,nonatomic) AVAudioPlayer *audioPlayer;
- (IBAction)ActionTakePicture:(id)sender;
- (IBAction)btnFrontCamera:(id)sender;
- (IBAction) btnFlash:(id)sender;
- (IBAction)btnCross:(id)sender;

- (IBAction)btnCancelImage:(id)sender;
- (IBAction)btnGallery:(id)sender;
@property (strong, nonatomic) IBOutlet UIButton *btnBlackShade;
- (IBAction)btnBlackShade:(id)sender;


@end
