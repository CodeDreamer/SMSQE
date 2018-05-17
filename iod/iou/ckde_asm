; Check if directory is empty   V2.00    1989  Tony Tebby  QJUMP

        section iou

        xdef    iou_ckde

        xref    iou_fmlw

        include 'dev8_keys_iod'
        include 'dev8_keys_err'
        include 'dev8_keys_chn'
        include 'dev8_keys_hdr'
;+++
; Check if directory is empty
;
;       d6 cr   drive / directory file ID
;       d7   s  byte count
;       a0 c  p base of channel block  
;       a2   s  buffer pointer
;       a3 c  p linkage block pointer
;       a4 c  p physical definition pointer
;
;       status return standard
;---
iou_ckde
reglist  reg     d3/d4/d5/a1
        movem.l reglist,-(sp)            ; save regs
        move.l  iod_hdrl(a4),d5          ; start from here!

ide_floop
        cmp.l   chn_feof(a0),d5          ; at end of file?
        bhs.s   ide_exit                 ; ... yes, no file found

        lea     chn_opwk(a0),a1          ; spare space for directory entry
        moveq   #hdr.len,d7              ; length to read
        jsr     iou_fmlw                 ; read directory entry
        bne.s   ide_exit                 ; ... not there
        tst.w   hdr_name+chn_opwk(a0)    ; no name?
        beq.s   ide_floop                ; ... yes

ide_fiu
        move.l  #err.fdiu,d0             ; file found in directory
ide_exit
        movem.l (sp)+,reglist            ; restore regs
        rts
        end
