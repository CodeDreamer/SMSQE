* Do dump    V2.01       1987  Tony Tebby
*
        section sdp
*
        xdef    sdp_do          do screen dump
        xdef    sdp_dosv        do screen dump in supervisor mode
*
        xref    sdp_byte        send byte
        xref    sdp_sbuf        send buffer
        xref    sdp_ctrl        send control sequence
*
        include dev8_sys_sdp_data
sdp_dosv
        move.l  sp,dp_pstk(a5)           ; save stack pointer
sdp_do
        clr.w   dp_bufpt(a5)             ; clear buffer

        moveq   #dp.pre,d0               ; set preamble
        bsr.l   sdp_ctrl                 ; send control sequence
        bne.l   dd_rts
                                ; set start addresses
        move.l  dp_stadd(a5),dp_raddr(a5)
        move.w  dp_stpix(a5),dp_pxrrp(a5)
                                ; and initial pointers and counters
        move.l  dp_bainc(a5),a2
        move.l  dp_patt(a5),a4
        move.w  dp_mask(a5),d7
        move.l  dp_randi(a5),d4

        move.w  dp_nrows(a5),dp_rowc(a5)
        clr.w   dp_ptrpt(a5)
        move.w  dp_ptrnm(a5),dp_ptrct(a5)
        move.b  dp_bitnw(a5),dp_bitwd(a5)
        move.b  dp_bitlw(a5),dp_bitwe(a5)
        bra.s   dd_rowlp
        page
dd_nxrow                 ; Move to next row, skipping a bit at a time
        move.w  dp_rbits(a5),dp_rbitc(a5); rbitc <- rbits

dd_nxbit                 ; Move to next output bit, this could be a new row of
                         ; the pattern or it could be a new pixel row.
                         ; First we move to a new row of pattern, and if this
                         ; exhausts the pattern, we reset the pointer and
                         ; counter, then move to next pixel row
        move.w  dp_ptrin(a5),d0          ; ptrpt <- ptrpt+ptrin
        add.w   d0,dp_ptrpt(a5)
        subq.w  #1,dp_ptrct(a5)          ; ptrct <- ptrct-1
        bgt.s   dd_nxrlp                 ; more>    nxrlp
        move.w  dp_ptrnm(a5),dp_ptrct(a5); no more> ptrct <- ptrnm
        move.w  dp_ptrsz(a5),d0          ;
        sub.w   d0,dp_ptrpt(a5)          ;          ptrpt <- ptrpt-ptrsz

                         ; pattern exhausted, move to next pixel
        move.w  dp_pxrdc(a5),d0          ; pxrrp <- pxrrp-pxrdc
        sub.w   d0,dp_pxrrp(a5)
        bge.s   dd_nxrlp                 ; more>    nxrlp

                         ; pixels exhausted, move to next address
        move.l  dp_rainc(a5),d0          ; no more> raddr <- raddr+rainc
        add.l   d0,dp_raddr(a5)
        addq.w  #8,dp_pxrrp(a5)          ;          pxrrp <- pxrrp+8

dd_nxrlp                 ; end of next row loop
        subq.w  #1,dp_rbitc(a5)          ; more bits?
        bgt.s   dd_nxbit                 ; yes>     nxbit
dd_rowlp                 ; end of row loop                       
        subq.w  #1,dp_rowc(a5)           ; more rows?
        bgt.s   dd_dorow                 ; yes>     do row
        blt.s   dd_done                  ; done> send epilogue

dd_lstrw                 ; setup short? last row
        move.b  dp_bitlr(a5),dp_bitwd(a5); bitwd <- bitlr
        move.b  dp_bitlc(a5),dp_bitwe(a5); bitwe <- bitlc
        bra.s   dd_dorow                 ; do row

dd_done                  ; all done send epilogue
        moveq   #dp.epi,d0               ; epilogue
        bsr.l   sdp_ctrl                 ; send control sequence
        beq.l   sdp_sbuf                 ; and buffer
        bra.s   dd_rts
dd_ok
        moveq   #0,d0                    ; set ok
dd_rts
        rts                              ; return
        page
dd_dorow                 ; Now we start the exciting business of doing one row
                         ; this is one (or more) passes of the print head.
                         ; For colour passes, the paper is not advanced
                         ; for multipass, there may be a small increment

        moveq   #dp.rpre,d0              ; row preamble
        bsr.l   sdp_ctrl                 ; send control sequence
        bne.s   dd_rts

        tst.w   dp_ncolr(a5)             ; colour?
        beq.s   dr_strow                 ; no>      strow
        clr.w   dp_ptrpt(a5)             ; yes>     ptrpt <- 0

dr_clrrw                 ; do colour row
        moveq   #dp.cpre,d0              ; colour preamble
        bsr.l   sdp_ctrl                 ; send control sequence
        bne.s   dd_rts

dr_strow                 ; set the start of row pointers to the current values
        move.l  dp_raddr(a5),dp_paddr(a5); paddr <- raddr  set address
        move.w  dp_pxrrp(a5),dp_pxprp(a5); pxprp <- pxrrp      pixel
        move.w  dp_ptrpt(a5),dp_ptppt(a5); ptppt <- ptrpt      pattern
        move.w  dp_ptrct(a5),dp_ptpct(a5); ptpct <- ptrct      ... counter
        move.w  dp_npass(a5),dp_passc(a5); passc <- npass      number of passes

dr_dopas                 ; do a pass
        moveq   #dp.ppre,d0              ; pass preamble
        bsr.l   sdp_ctrl                 ; send control sequence
        bne.s   dd_rts

        move.b  dp_bitwd(a5),dp_biten(a5); biten <- bitwd  set bit
        move.l  dp_paddr(a5),dp_waddr(a5); waddr <- paddr      address
        move.l  dp_waddr(a5),a1          ; baddr <- waddr
        move.w  dp_pxprp(a5),dp_pxwrp(a5); pxwrp <- pxprp      pixel
        move.w  dp_pxwrp(a5),d3          ; pxbrp <- pxwrp
        move.w  dp_ptppt(a5),dp_ptwpt(a5); ptwpt <- ptppt      pattern
        move.w  dp_ptwnm(a5),dp_ptwct(a5); ptwct <- ptwnm
        move.w  dp_ptwpt(a5),d5          ; ptbpt <- ptwpt 
        move.w  dp_ptpct(a5),d6          ; ptbct <- ptpct unless
        tst.b   dp_bitil(a5)             ; inline?
        beq.s   dp_dowrd                 ; no>  do words
        move.w  dp_ptbnm(a5),d6          ; yes> ptbct <- ptbnm
dp_dowrd
        move.w  dp_wrdrw(a5),dp_wrdct(a5); wrdct <- wrdrw      word count
        bsr.s   dd_dowrd                 ; do wrdct words
        bne.l   dd_rts

dr_endps                 ; end of pass
        moveq   #dp.pepi,d0              ; pass epilogue
        bsr.l   sdp_ctrl                 ; send control sequence
        bne.l   dd_rts

        subq.w  #1,dp_passc(a5)          ; another pass?
        ble.s   dr_endcl                 ; no>      end of colour/row
                                         ; yes>     nxtps
dr_nxtps                ; set up next pass
        move.w  dp_ptpin(a5),d0          ; ptppt <- ptppt+ptpin
        add.w   d0,dp_ptppt(a5)
        subq.w  #1,dp_ptpct(a5)          ; ptpct <- ptpct-1
        bgt.s   dr_dopas                 ; more>    do another pass
        move.w  dp_ptpnm(a5),dp_ptpct(a5); no more> ptpct <- ptpnm
        move.w  dp_ptpsz(a5),d0
        sub.w   d0,dp_ptppt(a5)          ;          ptppt <- ptppt-ptpsz

                         ; pattern exhausted, move to next pixel
        move.w  dp_pxpdc(a5),d0          ; pxprp <- pxprp-pxpdc
        sub.w   d0,dp_pxprp(a5)
        bge.l   dr_dopas                 ; more>    do another pass
        move.l  dp_painc(a5),d0          ; no more> paddr <- paddr+painc
        add.l   d0,dp_paddr(a5)
        addq.w  #8,dp_pxprp(a5)          ;          pxprp <- pxprp+8
        bra.l   dr_dopas                 ;          do another pass

dr_endcl                 ; end of colour/row
        move.w  dp_ncolr(a5),d2          ; colour?
        beq.s   dr_endrw                 ; no>      end of row
        moveq   #dp.cepi,d0              ; yes>     colour epilogue
        bsr.l   sdp_ctrl                 ;          send control sequence
        bne.l   dd_rts

        addq.w  #2,dp_ptrpt(a5)          ;          ptrpt <- ptrpt+2 next colour
        add.w   d2,d2
        cmp.w   dp_ptrpt(a5),d2          ;          last done?
        bgt.l   dr_clrrw                 ;          no>      clrrw

dr_endrw                 ; end of row
        moveq   #dp.repi,d0              ; row epilog
        bsr.l   sdp_ctrl                 ; send control sequence
        beq.l   dd_nxrow                 ; done>    next row
        bra.l   dd_rts
*
        page
dd_dowrd                 ; Do wrdct words
        subq.w  #1,dp_wrdct(a5)          ; next word
        blt.l   dd_ok                    ; done>    return OK
        bgt.s   dw_stwrd                 ; normal>  setup
        move.b  dp_bitwe(a5),dp_biten(a5); (short) final word

dw_stwrd                 ; Set up to do word
        moveq   #0,d1                    ; outwd <- iniwd
        move.b  dp_bitst(a5),d2          ; bitrp <- bitst

dw_dopix                 ; Do one bit of a word
        move.w  (a1),d0                  ; pixel <- (baddr)
        lsr.w   d3,d0                    ; pixel <- pixel>>pxbrp......84......21
        ror.w   #2,d0                    ;                      21......84......
        rol.b   #2,d0                    ;                      21............84
        rol.w   #2,d0                    ;                      ............8421
        and.w   d7,d0                    ;  0000000000000401 or 0000000000008021
        move.b  dp_map(a5,d0.w),d0       ; pixel <- map(pixel)

dw_dtbit                 ; add dot bit to word
        tst.l   d4                       ; random?
        bpl.s   dw_dtpat                 ; ... no
                         ; add random dot bit to word
        tst.b   d0                       ; zero?
        beq.s   dw_nxbit                 ; yes>     nxbit
        add.w   #dp.randa,d4             ; no>      randomise
        ror.w   #dp.randr,d4
        cmp.b   d4,d0                    ; value >= random?
        blo.s   dw_nxbit                 ; no>      nxbit
        bra.s   dw_bchg                  ; outwd.bitrp <- ^outwd.bitrp

dw_dtpat                 ; add pattern dot bit to word
        btst    d0,(a4,d5.w)             ; bit req?
        beq.s   dw_nxbit                 ; no>      nxbit

dw_bchg                  ; change bit in word
        bchg    d2,d1                    ; outwd.bitrp <- ^outwd.bitrp

dw_nxbit                 ; move to next bit in word
        cmp.b   dp_biten(a5),d2          ; bitrp=biten?
        beq.s   dw_wrwrd                 ; yes>     write word

        add.b   dp_bitin(a5),d2          ; bitrp <- bitrp+bitin
dw_nxpat                 ; move to next bit of pattern 
        add.w   dp_ptbin(a5),d5          ; ptbpt <- ptbpt+ptbin
        subq.w  #1,d6                    ; ptbct <- ptbct-1
        bgt.s   dw_dtbit                 ; more>    dtbit
        move.w  dp_ptbnm(a5),d6          ; no more> ptbct <- ptbnm
        sub.w   dp_ptbsz(a5),d5          ;          ptbpt <- ptbpt-ptbsz

                         ; Pattern exhausted, move to next pixel
        sub.w   dp_pxbdc(a5),d3          ; pxbrp <- pxbrp-pxbdc
        bge.s   dw_dopix                 ; more>    do pixel
        add.w   a2,a1                    ; no more> baddr <- baddr+bainc
        addq.w  #8,d3                    ;          pxbrp <- pxbrp+8
        bra.s   dw_dopix                 ;          do pixel

dw_wrwrd                 ; write word
        add.l   dp_addwd(a5),d1          ; outwd <- outwd+addwd    add odd bits
        move.w  dp_nbwrd(a5),d2          ; bits <- nbwrd
        tst.b   dp_msbyt(a5)             ; byte order
        beq.s   dw_wrlsb                 ; EQ       ls byte first

dw_wrmsb                 ; write bytes from msbyte
        rol.l   #8,d1                    ; rotate next byte in
        bsr.l   sdp_byte                 ; send byte
        bne.l   dd_rts  
        subq.w  #8,d2                    ; bits  <- bits-8
        ble.s   dw_nxwrd                 ; no more> next word
        bra.s   dw_wrmsb                 ; write byte

dw_wrlsb                 ; write bytes from lsbyte
        bsr.l   sdp_byte                 ; send byte
        bne.l   dd_rts
        subq.w  #8,d2                    ; bits  <- bits-8
        ble.s   dw_nxwrd                 ; no more> next word
        ror.l   #8,d1                    ; rotate next byte in
        bra.s   dw_wrlsb                 ; write byte

dw_nxwrd                 ; move to next word
        tst.b   dp_bitil(a5)             ; in line?
        bne.s   db_nxpat                 ; yes>     move to next bit pattern

                         ; move to pattern for next word
        move.w  dp_ptwin(a5),d0          ; ptwpt <- ptwpt+ptwin
        add.w   d0,dp_ptwpt(a5)
        subq.w  #1,dp_ptwct(a5)          ; ptwct <- ptwct-1
        bgt.s   dw_nxbad                 ; more>    reset next bit address
        move.w  dp_ptwnm(a5),dp_ptwct(a5); no more> ptwct <- ptwnm
        move.w  dp_ptwsz(a5),d0
        sub.w   d0,dp_ptwpt(a5)          ;          ptwpt <- ptwpt-ptwsz

dw_nxpix                 ; pattern exhausted, move to next pixel
        move.w  dp_pxwdc(a5),d0          ; pxwrp <- pxwrp-pxwdc
        sub.w   d0,dp_pxwrp(a5)
        bge.s   dw_nxbpx                 ; more>    set next bit pixel/address
        move.l  dp_wainc(a5),d0          ; no more> waddr <- waddr+wainc
        add.l   d0,dp_waddr(a5)
        addq.w  #8,dp_pxwrp(a5)          ;          pxwrp <- pxwrp+8

dw_nxbpx                 ; set next bit pixel/address
        move.w  dp_pxwrp(a5),d3          ; pxbrp <- pxwrp
dw_nxbad
        move.l  dp_waddr(a5),a1          ; baddr <- waddr

dw_nxprp                 ; set next pattern running pointer
        move.w  dp_ptwpt(a5),d5          ; ptbpt <- ptbpt
        move.w  dp_ptpct(a5),d6          ; ptbct <- ptpct
        bra.l   dd_dowrd                 ; do another word

db_nxpat                 ; move to next bit of pattern 
        add.w   dp_ptbin(a5),d5          ; ptbpt <- ptbpt+ptbin
        subq.w  #1,d6                    ; ptbct <- ptbct-1
        bgt.l   dd_dowrd                 ; more>    do next word
        move.w  dp_ptbnm(a5),d6          ; no more> ptbct <- ptbnm
        sub.w   dp_ptbsz(a5),d5          ;          ptbpt <- ptbpt-ptbsz

                         ; Pattern exhausted, move to next pixel
        sub.w   dp_pxbdc(a5),d3          ; pxbrp <- pxbrp-pxbdc
        bge.l   dd_dowrd                 ; more>    do next word
        add.w   a2,a1                    ; no more> baddr <- baddr+bainc
        addq.w  #8,d3                    ;          pxbrp <- pxbrp+8
        bra.l   dd_dowrd                 ;          do next word
        end
