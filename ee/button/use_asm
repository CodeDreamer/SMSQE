; Use Button Frame                V0.01   1989 Tony Tebby

        section button

        xdef    bt_use

        xref    bt_fralc
        xref    iou_achb

        include 'dev8_keys_err'
        include 'dev8_keys_chp'
        include 'dev8_keys_thg'
        include 'dev8_mac_assert'
        include 'dev8_ee_button_data'

;+++
; This routine is the internal USE routine for the Button Thing.
; If the Job does not already have an allocation in the frame, or a new
; allocation is required, it looks for a hole in the Button Frame and, if
; successful, allocates a usage block with the Size and Position of the button.
; If the Job does have an allocation, and it is big enough, then the allocation
; is unaltered. If it is not big enough, then the button is re-allocated.
;
;       d0  r   0 or ERR.ORNG if no room in frame, or ERR.FEX if re-allocated
;       d2 cr   button size / button origin
;       d3 c  s 0 new allocation, -ve long word for re-allocate
;       a0  r   base of usage block
;       a1 c  s thing linkage
;       a2  r   base of usage block
;---
bt_use
        add.l   #$00030001,d2            ; round size up
        and.l   #$fffcfffe,d2

        clr.l   -(sp)                    ; normal return
        tst.l   d3                       ; re-allocate?
        beq.s   btu_allc                 ; ... no
        lea     th_usage(a1),a0          ; usage list

btu_luse
        move.l  (a0),d0                  ; another?
        beq.s   btu_allc                 ; ... no
        move.l  d0,a0                    ; next usage link
        cmp.l   chp_ownr-thu_link(a0),d1 ; our owner?
        bne.s   btu_luse
        lea     -thu_link(a0),a0         ; base of block
        move.l  a0,(sp)                  ; ... keep it

        movem.l btt_wind(a0),d1/d3       ; old window
        cmp.w   d1,d2                    ; y large enough?
        bgt.s   btu_free                 ; ... no, free the block
        cmp.l   d1,d2                    ; x large enough?
        ble.l   btu_srt2                 ; ... yes, set d2/a2

btu_free
        jsr     bt_fralc                 ; free the allocation
btu_allc
        assert  btt_ncol,btt_nrow-2,btt_rinc-4
        movem.w btt_ncol(a1),d3/d4/d5    ; get basic parameters
        lea     btt_fram(a1),a2          ; first row of frame
        move.w  btt_yorg(a1),btt_rpos(a2); starts at origin

        tst.b   btt_rows(a1)             ; organised as rows?
        bne.s   btu_frow                 ; ... yes
        swap    d2                       ; ... no, swap x,y
        move.w  btt_xorg(a1),btt_rpos(a2)

btu_frow          ; find row large enough to accomodate button

        move.w  btt_rpos(a2,d5.w),d0     ; start of next row
        move.w  btt_rpos(a2),d6          ; start of this row
        sub.w   d6,d0                    ; max height of row
        cmp.w   d0,d2                    ; enough room?
        bgt.s   btu_nxrow                ; ... no

        swap    d2
        lea     btt_list(a2),a0          ; now check for available hole
        move.w  d3,d1                    ; ... this many times
btu_fcol
        move.w  (a0)+,d0
        tst.w   (a0)+                    ; filled?
        bne.s   btu_nxcol                ; ... yes
        sub.w   (a0),d0                  ; negative space
        beq.s   btu_nrrow                ; ... no room in row
        add.w   d2,d0
        ble.s   btu_spos                 ; ... yes, set the position
btu_nxcol
        subq.w  #1,d1
        bgt.s   btu_fcol

btu_nrrow
        swap    d2
btu_nxrow
        move.w  btt_nitm(a2),d0          ; anything in row?
        bne.s   btu_nxoff                ; ... yes
        move.w  btt_rpos(a2,d5.w),btt_rpos(a2) ... no, reset position

btu_nxoff
        tst.w   btt_nitm(a2,d5.w)        ; any objects in next row?
        bne.s   btu_nxradd               ; ... yes, carry on

        moveq   #0,d1                    ; minimum height
        lea     btt_list(a2),a0
        bra.s   btu_srend
btu_srloop
        assert  btt_ipos,btt_size-2,0
        move.l  (a0)+,d7                 ; size in lsw
        cmp.w   d1,d7
        ble.s   btu_srend
        move.w  d7,d1                    ; new largest
btu_srend
        dbra    d0,btu_srloop

        add.w   btt_rpos(a2),d1          ; start of next row
        move.w  d1,btt_rpos(a2,d5.w)     ; set it

btu_nxradd
        add.w   d5,a2                    ; next row
        subq.w  #1,d4
        bgt.s   btu_frow                 ; ... carry on

        moveq   #err.orng,d0             ; no room at the inn
        bra.s   btu_oops

btu_spos
        addq.w  #1,btt_nitm(a2)          ; one more item
        tst.w   d0                       ; any spare?
        beq.s   btu_ssiz                 ; ... exact fit, just set the size
        add.w   d5,a2                    ; go to end of list
btu_move
        move.l  -8(a2),-(a2)
        cmp.l   a0,a2                    ; all done yet?
        bgt.s   btu_move
        add.w   d2,(a0)                  ; this much extra to the right

btu_ssiz
        swap    d2
        move.w  d2,-(a0)                 ; fill in the height

        move.w  -(a0),d3
        swap    d3
        move.w  d6,d3                    ; complete position

        move.l  (sp),a0                  ; existing block
        move.l  a0,d0                    ; any?
        bne.s   btt_filb
        moveq   #btt_uend,d0             ; size of usage block
        jsr     iou_achb                 ; allocate it
        bne.s   btu_oops

btt_filb
        tst.b   btt_rows(a1)             ; organised in rows?
        bne.s   btu_sret                 ; ... yes
        swap    d2                       ; ... no, swap X and Y
        swap    d3
btu_sret
        movem.l d2/d3,btt_wind(a0)       ; set button window def
btu_srt2
        move.l  d3,d2                    ; and return the position
        move.l  a0,a2                    ; set usage block
        move.l  (sp)+,d0                 ; new allocation?
        beq.s   btu_rts                  ; ... yes
        moveq   #err.fex,d0             ; ... error stops it being linked in.
btu_rts
        rts

btu_oops
        addq.l  #4,sp                    ; remove return code
        rts
        end
