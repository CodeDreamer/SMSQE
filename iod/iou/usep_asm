; IO Device USE (prc interface) V2.02     1986  Tony Tebby  QJUMP

        section exten

        xdef    iou_usep

        xref    iou_ipar

        include 'dev8_keys_iod'
;+++
; This is the model of the procedure RAM_USE, FLP_USE etc.
;
;       XXX_USE yyy     or   XXX_USE 
;
;       d0 cr   offset of device linkage from thing / error status
;       a1 c  p parameter list
;       a2 c  p thing linkage
;
;       status return standard
;---
iou_usep
ius.reg reg     d1/a0
        lea     iod_dnam+2(a2,d0.l),a0   ; standard name
        move.l  4(a1),d1                 ; any parameter?
        beq.s   ius_upnam                ; ... no
        move.l  d1,a0                    ; ... yes, this is it
        cmp.w   #3,(a0)+                 ; 3 characters long?
        bne.s   iou_ipar                 ; ... oops
ius_upnam
        move.l  (a0),d1                  ; get new name
        and.l   #$5f5f5f00,d1            ; in upper case
        add.b   #'0',d1                  ; ending with '0'

        move.l  d1,iod_dnus+2(a2,d0.l)   ; set new name (but not length)
        moveq   #0,d0
        rts
        end
