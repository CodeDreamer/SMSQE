; Free Item in Button Frame                V0.01   1989 Tony Tebby

        section button

        xdef    bt_free
        xdef    bt_ffree
        xdef    bt_fralc

        xref    iou_rchb

        include 'dev8_keys_thg'
        include 'dev8_ee_button_data'
        include 'dev8_mac_assert'

;+++
; This routine is the internal FREE routine for the Button Thing.
; It finds the appropriate usage block, then frees the item in the button
; frame and throws the usage block away. If it cannot find the right usage
; block, it throws the first one away.
;
;       d0  r   0
;       d1    s
;       a0 cr   base of first usage block / block thown away
;       a1 c  p base of thing linkage
;       a2 c  p base of usage block / or zero for first one
;---
bt_free
        move.l  a2,d0            ; any usage block specified?
        beq.s   bt_ffree         ; ... no, force free

        move.l  a0,d1            ; keep first block safe
btf_fuse
        cmp.l   a2,a0            ; the right one?
        beq.s   bt_ffree         ; ... yes, throw it away
        move.l  thu_link(a0),d0  ; end of list
        beq.s   btf_rest         ; ... yes, restore
        move.l  d0,a0
        sub.w   #thu_link,a0
        bra.s   btf_fuse

btf_rest
        move.l  d1,a0            ; restore first block

;+++
; This routine is the internal FORCE FREE routine for the Button Thing.
; It frees the item in the button frame and throws the usage block away.
;
;       d0  r   0
;       a0 c  s base of usage block
;       a1 c  p base of thing linkage
;---
bt_ffree
        bsr.s   bt_fralc                 ; free allocation
        jmp     iou_rchb                 ; return block

;+++
; This routine frees a button frame allocation
;
;       a0 c  p base of usage
;       a1 c  p base of thing linkage
;       all other registers preserved
;       status return arbitrary
;---
bt_fralc
btf.reg reg     d0/d1/d4/d5/a3/a4
        movem.l btf.reg,-(sp)
        move.l  btt_wind+4(a0),d1        ; window origin
        move.w  btt_ncol(a1),d4          ; number of columns (max)
        move.w  btt_rinc(a1),d5          ; row inc
        lea     btt_fram(a1),a3
        tst.b   btt_rows(a1)             ; organised in rows?
        bne.s   btf_frow                 ; yes, find row
        swap    d1

btf_frow
        cmp.w   btt_rpos(a3),d1          ; row position
        beq.s   btf_deci                 ; the right one
        blt.s   btf_exit                 ; !!!
        add.w   d5,a3                    ; next row
        bra.s   btf_frow

btf_deci
        assert  btt_rpos,btt_nitm-2,btt_list-4,0
        subq.l  #1,(a3)+                 ; one fewer items in row
        bgt.s   btf_item
        move.l  -btt_list(a3,d5.w),-btt_list(a3); set start equal to next row
                     ; ***** this is not very good but try it for now
btf_item
        moveq   #1,d0                    ; preset no previous free
        swap    d1                       ; look for item position

btf_litm
        cmp.w   (a3)+,d1                 ; the right item?
        beq.s   btf_ritm                 ; remove item
        blt.s   btf_exit                 ; !!!
        move.w  (a3)+,d0                 ; save status of previous item
        subq.w  #1,d4                    ; one fewer items to look
        bgt.s   btf_litm
        bra.s   btf_exit                 ; !!!

btf_ritm
        clr.w   (a3)+                    ; no item now
        move.l  a3,a4                    ; start copying at this item
        tst.w   d0                       ; was previous empty?
        bne.s   btf_cknx                 ; ... no, check next
        subq.w  #4,a3                    ; ... yes, forget this one
btf_cknx
        tst.w   btt_size(a4)             ; is next empty?
        bne.s   btf_copy
        addq.w  #4,a4                    ; forget next
        subq.w  #1,d4
        bra.s   btf_copy

btf_cloop
        move.l  (a4)+,(a3)+              ; copy down
btf_copy
        subq.w  #1,d4
        bge.s   btf_cloop

        subq.l  #4,a4                    ; another to copy?
        cmp.l   a3,a4
        ble.s   btf_exit                 ; ... no
        move.l  (a4),(a3)                ; ... yes

btf_exit
        movem.l (sp)+,btf.reg
        rts

        end
