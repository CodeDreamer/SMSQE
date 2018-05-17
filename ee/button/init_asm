; Initialise Button Things              V0.00   1989 Tony Tebby

        xdef    bt_init

        xref    bt_name
        xref    bt_use
        xref    bt_free
        xref    bt_ffree
        xref    gu_achpp
        xref    gu_thzlk
        xref    gu_iowp

        xref    bt_xorg
        xref    bt_rows
        xref.l  bt_vers

        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qdos_io'
        include 'dev8_mac_assert'
        include 'dev8_ee_button_data'

        section version

        dc.w    ' V'
bti_ver dc.l    bt_vers
        dc.w    $200a

        section init
        xref    ini_error

;+++
; This segment sets up the Button Thing.
;
;       d1-d7   scratch
;       a0 c  p usable console channel ID
;       a1-a2   scratch
;
;       status  nz jumps to ini_error
;       status  z  falls through to next initialisation segment
;---
bt_init
        move.l  a0,-(sp)
        sub.l   a0,a0
        moveq   #iop.pick,d0             ; pick
        moveq   #0,d1
        moveq   #0,d2
        jsr     gu_iowp

        moveq   #iop.flim,d0             ; find limits
        subq.l  #8,sp
        move.l  sp,a1
        jsr     gu_iowp
        addq.l  #8,sp
        movem.l bt_xorg,d1/d2            ; x,y position, x,y size
        bne.l   bti_exit

        add.l   d1,d2                    ; x,y max
        cmp.w   2(a1),d2                 ; y too large?
        ble.s   bti_chky
        clr.w   d1                       ; full height
        move.w  2(a1),d2
bti_chky
        swap    d2
        cmp.w   (a1),d2                  ; x too large
        ble.s   bti_sets
        move.w  (a1),d2                  ; full width
        ext.l   d1
bti_sets
        swap    d2
        sub.l   d1,d2                    ; x,y sizes

        move.l  d2,d3
        clr.w   d3
        swap    d3
        moveq   #0,d4
        move.w  d2,d4
        divu    #btt.minx,d3             ; max columns
        divu    #btt.miny,d4             ; max rows

        move.b  bt_rows,d7               ; rows or columns
        bne.s   bti_ssiz                 ; ... rows
        exg     d3,d4
bti_ssiz
        move.w  d3,d5                    ; row increment
        addq.w  #btt_list/4+2,d5         ; start of item list + 2 spare items
        lsl.w   #2,d5                    ; four bytes per item

        move.w  d4,d6
        mulu    d5,d6
        moveq   #btt_fram+8,d0           ; total thing inc one extra row start
        add.l   d6,d0
        jsr     gu_achpp                 ; allocate
        bne.s   bti_exit

        lea     th_thing(a0),a1          ; fill up from here
        lea     btt_flag(a0),a2
        move.l  a2,(a1)+
        lea     bt_use,a2                ; use thing
        move.l  a2,(a1)+
        lea     bt_free,a2               ; free thing
        move.l  a2,(a1)+
        lea     bt_ffree,a2
        move.l  a2,(a1)+
        addq.l  #th_verid-th_ffree-4,a1
        move.l  bti_ver,(a1)+            ; version
        lea     bt_name,a2
        moveq   #btt_flag-th_name-1,d0
bti_nloop
        move.b  (a2)+,(a1)+              ; name
        dbra    d0,bti_nloop

        move.l  #thh.flag,(a1)+          ; flag
        add.w   #btt_xorg-btt_flag-4,a1  ; type and spare
        move.l  d1,(a1)+                 ; origin
        move.l  d2,(a1)+                 ; size
        move.w  d3,(a1)+                 ; max columns
        move.w  d4,(a1)+                 ; max rows
        move.w  d5,(a1)+                 ; row increment
        move.b  d7,(a1)                  ; row organisation
        bne.s   bti_sframe
        swap    d1
        swap    d2
bti_sframe
        addq.l  #btt_fram-btt_rows,a1
        add.l   d1,d2                    ; now upper limit
        move.w  d2,d5                    ; we need max x and y
        swap    d5                       ; max y in msw
        clr.w   d5
        clr.w   d1                       ; min x in msw
        clr.w   d2                       ; max x in msw
        bra.s   bti_erow

bti_ncol
        move.l  d2,(a1)+                 ; max x
bti_ecol
        dbra    d0,bti_ncol

bti_erow
        move.l  d5,(a1)+                 ; max y
        move.l  d1,(a1)+                 ; min x
        move.w  d3,d0                    ; reset column count
        dbra    d4,bti_ncol

        jsr     gu_thzlk                 ; link in thing
bti_exit
        move.l  (sp)+,a0
        bne.l   ini_error
        end
