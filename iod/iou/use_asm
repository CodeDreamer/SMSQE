; IO Device USE use name   V2.02     1986  Tony Tebby  QJUMP

        section exten

        xdef    iou_use

        xref    ut_gxnm1
        xref    iou_flnk
        xref    iou_ipar

        include 'dev8_keys_err'
        include 'dev8_keys_iod'
        include 'dev8_keys_sys'
        include 'dev8_keys_qdos_sms'
;+++
; This is the model of the SuperBASIC command RAM_USE, FLP_USE etc.
;
;       XXX_USE yyy     or   XXX_USE 
;
;       d1-d3   scratch
;       d7 c    three character + '0' device name
;       a0      scratch
;       a3 c    SuperBASIC parameter pointer
;       a5 c    SuperBASIC parameter pointer
;       a6 c  p SuperBASIC variables pointer
;
;       status return standard
;---
iou_use
        move.l  d7,d6                    ; assume reset
        cmp.l   a3,a5
        beq.s   ius_look                 ; ... it is

        bsr.l   ut_gxnm1                 ; get a string
        bne.s   ius_rts                  ; ... oops
        subq.w  #3,(a6,a1.l)             ; 3 characters long
        bne.s   iou_ipar                 ; ... oops
        move.l  2(a6,a1.l),d6            ; get new name
        and.l   #$5f5f5f00,d6            ; in upper case
        add.b   #'0',d6                  ; ending with '0'

ius_look
        bsr.s   iou_flnk                 ; find linkage
        bne.s   ius_rts
ius_set
        move.l  d6,iod_dnus-iod_iolk+2(a0) ; set new name
        moveq   #0,d0
ius_rts
        rts
        end
