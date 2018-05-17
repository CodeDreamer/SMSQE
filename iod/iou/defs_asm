; Additional definitions for IOU routines

        section iou

        xdef    iou_

;+++
; The IOU routines make a number of assummptions about the filing system
; device data structures over and above those of QDOS.
;
; Drive Definition
;
; iod_mnam equ    $0016   string  Medium NAMe (up to 10 characters long)
; iod_nrfl equ    $0022   byte    NumbeR of FiLes open on this medium
; iod_ftyp equ    $0023   byte    Format TYPe, 0=QDOS Mdv, QL5A or QLWA
; iod_rdln equ    $0024   long    Root Directory LeNgth
; iod_rdid equ    $0028   word    Root Directory file ID
; iod_hdrl equ    $002c   long    inbuilt file HeaDeR Length
; 
; The first two are standard, the others should be set by the IOU_CKOP
; routine when a medium is changed. IOD_RTDL is updated by the IOU_OPEN
; routine.
;
;
; Standard channel block for IOU routines
;
; chn_link equ    $0018 ; long    LINKed list of channel blocks
; chn_accs equ    $001c ; byte    ACCeSs mode
; chn_drid equ    $001d ; byte    DRive ID
; chn_qdid equ    $001e ; word    QDOS thinks this is file ID
; chn_fpos equ    $0020 ; long    File POSition
; chn_feof equ    $0024 ; long    File EOF
; chn_csb  equ    $0028 ; long    current slave block
; chn_updt equ    $002c ; byte    file UPDaTed
; chn_used equ    $002d ; byte    file USED (msbit)
; chn_name equ    $0032 ; string  file NAME
; chn.nmln equ      $24 ;         max file NaMe LeNgth
; chn_ddef equ    $0058 ; long    pointer to Drive DEFinition
; chn_drnr equ    $005c ; word    DRive NumbeR
; chn_flid equ    $005e ; word    FiLe ID
;
; chn_sdid equ    $0062 ; word    (Sub-)Directory ID
; chn_sdps equ    $0064 ; long    (Sub-)Directory entry position
; chn_spr  equ    $0068 ;         $38 bytes spare
; chn_fend equ    $00a0 ;         File system channel end
;
; CHN_USED is used by the IOU_IO routine to set the initial file pointer on
; the first IO call. CHN_DRNM and CHN_SDID are set and used by IOU routines.
;
;
; Standard directory driver entry list (in linkage block).
;
; iod_chek equ    $0042           check all slave blocks read
; iod_flsh equ    $0048           flush all buffers
; iod_info equ    $004e           get occupancy information
; iod_load equ    $0054           load
; iod_save equ    $005a           save
; iod_trnc equ    $0060           truncate
; iod_lcbf equ    $0066           locate buffer
; iod_albf equ    $006c           locate / allocate buffer
; iod_upbf equ    $0072           mark buffer updated
; iod_alfs equ    $0078           allocate first sector
; iod_ckop equ    $007e           check medium for open operation
; iod_fdrv equ    $0084           format drive
;---
iou_
        end
