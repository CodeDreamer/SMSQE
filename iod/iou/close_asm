; Close Operations   V2.01     1987  Tony Tebby   QJUMP

        section iou

        xdef    iou_close

        xref    iou_flsh

        include 'dev8_keys_chn'
        include 'dev8_keys_iod'
        include 'dev8_keys_sys'
        include 'dev8_keys_qlv'
        include 'dev8_mac_assert'

;+++
; General close operation.
;     If direct sector: sets normal.
;     Flushes if updated.
;     Decrements number of files open.
;     Unlinks channel from list.
;
;       d6   s  drive number / file id
;       a0 c    channel base address
;       a2   s  internal buffer address
;       a3 c  p linkage block address
;       a4    p pointer to physical definition
;       a5    p pointer to map
;
;---
iou_close
reglist  reg    d4-d7/a0/a3/a4/a5
        movem.l reglist,-(sp)            ; save lots of registers

        assert  chn_drnr,chn_flid-2
        move.l  chn_drnr(a0),d6          ; msw is drive / file ID
        move.l  chn_ddef(a0),a4
        move.l  iod_map(a4),a5
        tst.b   iod_ftyp(a4)             ; format type?
        bge.s   icl_norm                 ; normal

        not.b   iod_ftyp(a4)             ; direct sector access cancelled
        tst.b   chn_updt(a0)             ; updated?
        beq.s   icl_unlk                 ; ... no
        clr.b   iod_drst(a4)             ; ... yes, force restart
        bra.s   icl_unlk                 ; and unlink

icl_norm
        tst.b   chn_updt(a0)             ; file updated?
        beq.s   icl_unlk                 ; ... no, unlink

        moveq   #0,d3                    ; first entry!!
        bsr.l   iou_flsh                 ; flush file buffers

icl_unlk
        subq.b  #1,iod_nrfl(a4)          ; one fewer files

        lea     chn_link(a0),a0          ; point to next link
        lea     sys_fsch(a6),a1          ; start of linked list of channels
        move.w  mem.rlst,a2              ; and unlink this one
        jsr     (a2)
        lea     -chn_link(a0),a0
        move.w  mem.rchp,a2              ; and remove from heap
        jsr     (a2)
        movem.l (sp)+,reglist            ; restore registers
        rts
        end
